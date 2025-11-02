package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.Items.AtmosphereUpgradeItem;
import com.hoshino.cti.Screen.menu.AtmosphereCondensatorMenu;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PMachineEnergySync;
import com.hoshino.cti.netwrok.packet.PMachineFluidSync;
import com.hoshino.cti.recipe.AtmosphereCondensorRecipe;
import com.hoshino.cti.recipe.RecipeMap;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.ctiEnergyStore;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.hoshino.cti.util.BiomeUtil.getBiomeKey;

public class AtmosphereCondensatorEntity extends GeneralMachineEntity implements MenuProvider {
    public AtmosphereCondensatorEntity(BlockPos blockPos, BlockState blockState) {
        super(CtiBlockEntityType.Atmosphere_condensator.get(), blockPos, blockState);
        this.DATA = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AtmosphereCondensatorEntity.this.PROGRESS;
                    case 1 -> AtmosphereCondensatorEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AtmosphereCondensatorEntity.this.PROGRESS = value;
                    case 1 -> AtmosphereCondensatorEntity.this.MAX_PROGRESS = value;
                }
                ;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public ContainerData DATA;
    public int PROGRESS = 0;
    public int MAX_PROGRESS = 100;
    protected Component DISPLAY_NAME = Component.translatable("cti.machine.atmosphere_condensator").withStyle(ChatFormatting.DARK_PURPLE);
    protected int MAX_ENERGY = 75000000;
    protected int MAX_TRANSFER = 75000000;
    protected int BASE_ENERGY_PERTICK = 750000;
    public int CurrentEnergy = 0;


    public final FluidTank FLUID_TANK = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return FLUID_TANK.getFluid().getFluid() == stack.getFluid() || FLUID_TANK.isEmpty();
        }
    };

    public final ctiEnergyStore ENERGY_STORAGE = new ctiEnergyStore(getMaxEnergy(), getMaxTransfer()) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(4) {
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot < 4) {
                return !stack.isEmpty() && stack.getItem() instanceof AtmosphereUpgradeItem;
            } else return false;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot < 4 ? 1 : 64;
        }
    };

    public void setFluid(FluidStack stack) {
        this.FLUID_TANK.setFluid(stack);
    }

    public FluidStack getFluid() {
        return this.FLUID_TANK.getFluid();
    }

    public int getMaxEnergy() {
        return this.MAX_ENERGY;
    }

    public int getMaxTransfer() {
        return this.MAX_TRANSFER;
    }

    public int getMaxProgress() {
        return this.MAX_PROGRESS;
    }

    public int getEnergyPerTick() {
        return (int) (this.BASE_ENERGY_PERTICK * getPowerFactor(this));
    }


    public Component getDisplayName() {
        return DISPLAY_NAME;
    }

    private LazyOptional<ItemStackHandler> LazyitemStackHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> LazyenergyHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (direction == null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyitemStackHandler.cast();
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyenergyHandler.cast();
        }
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> FLUID_TANK);
        LazyitemStackHandler = LazyOptional.of(() -> itemStackHandler);
        LazyenergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
        LazyitemStackHandler.invalidate();
        LazyenergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("energy_store", ENERGY_STORAGE.serializeNBT());
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("atmosphere_condensator.progress", this.PROGRESS);
        nbt = FLUID_TANK.writeToNBT(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        ENERGY_STORAGE.deserializeNBT(nbt.get("energy_store"));
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.PROGRESS = nbt.getInt("atmosphere_condensator.progress");
        FLUID_TANK.readFromNBT(nbt);
        super.load(nbt);
    }


    @Nullable
    public ctiEnergyStore getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void dropItem() {
        if (itemStackHandler.getSlots() > 0) {
            SimpleContainer container = new SimpleContainer(itemStackHandler.getSlots());
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                container.setItem(i, itemStackHandler.getStackInSlot(i));
            }
            if (this.level != null) {
                Containers.dropContents(this.level, this.worldPosition, container);
            }
        }
    }

    public static float getPowerFactor(AtmosphereCondensatorEntity entity) {
        float factor = 1;
        ItemStackHandler handler = entity.itemStackHandler;
        if (handler != null) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getItem() instanceof AtmosphereUpgradeItem item) {
                    factor *= item.POWER_FACTOR;
                }
            }
        }
        return factor;
    }

    public static float getSpeedFactor(AtmosphereCondensatorEntity entity) {
        float factor = 1;
        ItemStackHandler handler = entity.itemStackHandler;
        if (handler != null) {
            for (int i = 0; i < 4; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getItem() instanceof AtmosphereUpgradeItem item) {
                    factor += item.SPEED_FACTOR;
                }
            }
        }
        return factor;
    }


    public static void tick(Level level, BlockPos blockPos, BlockState state, AtmosphereCondensatorEntity entity) {
        if (level.isClientSide) {
            return;
        }
        CtiPacketHandler.sendToClient(new PMachineEnergySync(entity.ENERGY_STORAGE.getEnergyStored(), entity.getBlockPos()));
        CtiPacketHandler.sendToClient(new PMachineFluidSync(entity.getFluid(), entity.getBlockPos()));
        ResourceKey<Biome> biomekey = getBiomeKey(level.getBiome(blockPos));
        if (biomekey == null) {
            return;
        }
        if (entity.ENERGY_STORAGE.getEnergyStored() <= entity.getEnergyPerTick()) {
            return;
        }
        FluidStack output = getOutPut(biomekey, level);
        if (output.isEmpty()) {
            return;
        }
        if (!canOutput(entity, output)) {
            return;
        }
        int Speed = 1;
        BlockPos pos = blockPos;
        for (int i = 0; i < 4; i++) {
            pos = pos.above();
            BlockState blockstate = level.getBlockState(pos);
            if (!(blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR))) {
                break;
            }
            ((ServerLevel) level).sendParticles(ParticleTypes.DOLPHIN, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1, 0.25, 0.25, 0.25, 0.05);
            Speed++;
        }
        Speed = (int) (getSpeedFactor(entity) * Speed);
        entity.PROGRESS = Mth.clamp(entity.PROGRESS + Speed, 0, entity.MAX_PROGRESS);
        entity.ENERGY_STORAGE.extractEnergy(entity.getEnergyPerTick(), false);
        if (entity.PROGRESS == entity.MAX_PROGRESS) {
            entity.PROGRESS = 0;
            Output(entity, output);
            setChanged(level, blockPos, state);
        }
    }

    public static boolean canOutput(AtmosphereCondensatorEntity entity, FluidStack stack) {
        return entity.FLUID_TANK.isFluidValid(stack) && entity.FLUID_TANK.getSpace() >= stack.getAmount();
    }

    public static void Output(AtmosphereCondensatorEntity entity, FluidStack output) {
        entity.FLUID_TANK.fill(output, IFluidHandler.FluidAction.EXECUTE);
    }

    public static FluidStack getOutPut(ResourceKey<Biome> biomekey, Level level) {
        List<AtmosphereCondensorRecipe> recipeList = List.copyOf(RecipeMap.CondensorRecipeList);
        for (AtmosphereCondensorRecipe recipe : recipeList) {
            ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(recipe.getBiome()));
            if (key.equals(biomekey)) {
                return recipe.getFluid();
            }
        }
        return FluidStack.EMPTY;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        CtiPacketHandler.sendToClient(new PMachineEnergySync(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos()));
        CtiPacketHandler.sendToClient(new PMachineFluidSync(this.getFluid(), this.getBlockPos()));
        return new AtmosphereCondensatorMenu(i, inventory, this, this.DATA);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemStackHandler;
    }
}
