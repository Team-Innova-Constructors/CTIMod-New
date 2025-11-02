package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.Blocks.Machine.AtmosphereExtractorBlock;
import com.hoshino.cti.Items.AtmosphereUpgradeItem;
import com.hoshino.cti.Screen.menu.AtmosphereExtractorMenu;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PMachineEnergySync;
import com.hoshino.cti.recipe.AtmosphereExtractorRecipe;
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
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static com.hoshino.cti.util.BiomeUtil.getBiomeKey;

public class AtmosphereExtractorEntity extends GeneralMachineEntity implements MenuProvider {
    public AtmosphereExtractorEntity(BlockPos blockPos, BlockState blockState) {
        super(CtiBlockEntityType.Atmosphere_extractor.get(), blockPos, blockState);
        this.DATA = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AtmosphereExtractorEntity.this.PROGRESS;
                    case 1 -> AtmosphereExtractorEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AtmosphereExtractorEntity.this.PROGRESS = value;
                    case 1 -> AtmosphereExtractorEntity.this.MAX_PROGRESS = value;
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
    protected Component DISPLAY_NAME = Component.translatable("cti.machine.atmosphere_extractor").withStyle(ChatFormatting.DARK_PURPLE);
    protected int MAX_ENERGY = 75000000;
    protected int MAX_TRANSFER = 75000000;
    protected int BASE_ENERGY_PERTICK = 750000;
    public int CurrentEnergy = 0;


    public final ctiEnergyStore ENERGY_STORAGE = new ctiEnergyStore(getMaxEnergy(), getMaxTransfer()) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(5) {
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


    private LazyOptional<ItemStackHandler> LazyitemStackHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> LazyenergyHandler = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap = Map.of(
            Direction.DOWN, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> i == 4, (i, s) -> false)),
            Direction.NORTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> i == 4, (i, s) -> false)),
            Direction.EAST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> i == 4, (i, s) -> false)),
            Direction.WEST, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> i == 4, (i, s) -> false)),
            Direction.SOUTH, LazyOptional.of(() -> new WrappedHandler(itemStackHandler, (i) -> i == 4, (i, s) -> false))
    );

    public Component getDisplayName() {
        return DISPLAY_NAME;
    }


    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            if (direction == null) {
                return LazyitemStackHandler.cast();
            } else if (directionWrappedHandlerMap.containsKey(direction)) {
                Direction locDir = this.getBlockState().getValue(AtmosphereExtractorBlock.FACING);
                if (direction == Direction.UP || direction == Direction.DOWN) {
                    return directionWrappedHandlerMap.get(direction).cast();
                }
                return switch (locDir) {
                    default -> directionWrappedHandlerMap.get(direction.getOpposite()).cast();
                    case WEST -> directionWrappedHandlerMap.get(direction.getCounterClockWise()).cast();
                    case EAST -> directionWrappedHandlerMap.get(direction.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(direction).cast();
                };
            }
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyenergyHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyitemStackHandler = LazyOptional.of(() -> itemStackHandler);
        LazyenergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyitemStackHandler.invalidate();
        LazyenergyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("energy_store", ENERGY_STORAGE.serializeNBT());
        nbt.putInt("atmosphere_extractor.progress", this.PROGRESS);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        ENERGY_STORAGE.deserializeNBT(nbt.get("energy_store"));
        this.PROGRESS = nbt.getInt("atmosphere_extractor.progress");
        super.load(nbt);
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

    @Nullable
    public ctiEnergyStore getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public static float getPowerFactor(AtmosphereExtractorEntity entity) {
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

    public static float getSpeedFactor(AtmosphereExtractorEntity entity) {
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


    public static void tick(Level level, BlockPos blockPos, BlockState state, AtmosphereExtractorEntity entity) {
        if (level.isClientSide) {
            return;
        }
        CtiPacketHandler.sendToClient(new PMachineEnergySync(entity.ENERGY_STORAGE.getEnergyStored(), entity.getBlockPos()));
        ResourceKey<Biome> biomekey = getBiomeKey(level.getBiome(blockPos));

        if (biomekey == null) {
            return;
        }
        if (entity.ENERGY_STORAGE.getEnergyStored() <= entity.getEnergyPerTick()) {
            return;
        }
        ItemStack output = getOutPut(biomekey, level);
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
        Speed = (int) (Speed * getSpeedFactor(entity));
        entity.PROGRESS = Mth.clamp(entity.PROGRESS + Speed, 0, entity.MAX_PROGRESS);
        entity.ENERGY_STORAGE.extractEnergy(entity.getEnergyPerTick(), false);
        if (entity.PROGRESS == entity.MAX_PROGRESS) {
            entity.PROGRESS = 0;
            Output(entity, output);
            setChanged(level, blockPos, state);
        }
    }

    public static ItemStack getOutPut(ResourceKey<Biome> biomekey, Level level) {
        List<AtmosphereExtractorRecipe> recipeList = List.copyOf(RecipeMap.ExtractorRecipeList);
        for (AtmosphereExtractorRecipe recipe : recipeList) {
            ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(recipe.getBiome()));
            if (key.equals(biomekey)) {
                return recipe.getResultItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean canOutput(AtmosphereExtractorEntity entity, ItemStack stack) {
        SimpleContainer container = new SimpleContainer(1);
        container.setItem(0, entity.itemStackHandler.getStackInSlot(4));
        return container.isEmpty() || (container.getItem(0).is(stack.getItem()) && container.getItem(0).getCount() + stack.getCount() <= 64);
    }

    public static void Output(AtmosphereExtractorEntity entity, ItemStack output) {
        ItemStack stack = entity.itemStackHandler.getStackInSlot(4);
        ItemStack outputStack = new ItemStack(output.getItem(), output.getCount() + stack.getCount());
        entity.itemStackHandler.setStackInSlot(4, outputStack);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        CtiPacketHandler.sendToClient(new PMachineEnergySync(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos()));
        return new AtmosphereExtractorMenu(i, inventory, this, this.DATA);
    }


    @Override
    public ItemStackHandler getItemHandler() {
        return itemStackHandler;
    }
}
