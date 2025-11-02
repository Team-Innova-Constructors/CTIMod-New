package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.Blocks.Machine.ReactorNeutronCollectorBlock;
import com.hoshino.cti.Screen.menu.ReactorNeutronCollectorMenu;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PMachineEnergySync;
import com.hoshino.cti.recipe.ReactorNeutronCollectorRecipe;
import com.hoshino.cti.recipe.RecipeMap;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.DimensionConstants;
import com.hoshino.cti.util.EmptyHandlers;
import com.hoshino.cti.util.ctiEnergyStore;
import committee.nova.mods.avaritia.init.registry.ModItems;
import mekanism.api.Action;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registries.MekanismGases;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class ReactorNeutronCollectorEntity extends GeneralMachineEntity implements MenuProvider {
    public ReactorNeutronCollectorEntity(BlockPos blockPos, BlockState blockState) {
        super(CtiBlockEntityType.REACTOR_NEUTRON_COLLECTOR.get(), blockPos, blockState);
        this.DATA = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ReactorNeutronCollectorEntity.this.PROGRESS;
                    case 1 -> ReactorNeutronCollectorEntity.this.MAX_PROGRESS;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ReactorNeutronCollectorEntity.this.PROGRESS = value;
                    case 1 -> ReactorNeutronCollectorEntity.this.MAX_PROGRESS = value;
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
    public int MAX_PROGRESS = 100000000;
    protected Component DISPLAY_NAME = Component.translatable("cti.machine.reactor_neutron_collector").withStyle(ChatFormatting.DARK_PURPLE);
    protected int MAX_ENERGY = 2000000000;
    protected int MAX_TRANSFER = 2000000000;
    protected int BASE_ENERGY_PERTICK = 100000;
    protected int BASE_SODIUM = 100000000;
    protected int BASE_SODIUM_PERTICK = 100000;
    public int CurrentEnergy = 0;


    public final ctiEnergyStore ENERGY_STORAGE = new ctiEnergyStore(getMaxEnergy(), getMaxTransfer()) {
        @Override
        public void onEnergyChange() {
            setChanged();
        }
    };

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return true;
        }
    };

    private final IGasHandler gasHandler = new IGasHandler() {

        @Override
        public int getTanks() {
            return 0;
        }

        @Override
        public GasStack getChemicalInTank(int i) {
            return GasStack.EMPTY;
        }

        @Override
        public void setChemicalInTank(int i, GasStack stack) {

        }

        @Override
        public long getTankCapacity(int i) {
            return 0;
        }

        @Override
        public boolean isValid(int i, GasStack stack) {
            return false;
        }

        @Override
        public GasStack insertChemical(int i, GasStack stack, Action action) {
            return GasStack.EMPTY;
        }

        @Override
        public GasStack extractChemical(int i, long l, Action action) {
            return GasStack.EMPTY;
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
        return this.BASE_ENERGY_PERTICK;
    }


    private LazyOptional<ItemStackHandler> LazyitemStackHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> LazyenergyHandler = LazyOptional.empty();
    private LazyOptional<IGasHandler> LazyGasHandler = LazyOptional.empty();

    public Component getDisplayName() {
        return DISPLAY_NAME;
    }


    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            return LazyitemStackHandler.cast();
        }
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyenergyHandler.cast();
        }
        Direction locDir = this.getBlockState().getValue(ReactorNeutronCollectorBlock.FACING);
        if (capability == Capabilities.GAS_HANDLER && (direction == locDir.getClockWise() || direction == locDir.getCounterClockWise())) {
            return LazyGasHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyitemStackHandler = LazyOptional.of(() -> itemStackHandler);
        LazyenergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        LazyGasHandler = LazyOptional.of(() -> gasHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyitemStackHandler.invalidate();
        LazyenergyHandler.invalidate();
        LazyGasHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.put("energy_store", ENERGY_STORAGE.serializeNBT());
        nbt.putInt("reactor_neutron_collector.progress", this.PROGRESS);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        ENERGY_STORAGE.deserializeNBT(nbt.get("energy_store"));
        this.PROGRESS = nbt.getInt("reactor_neutron_collector.progress");
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


    public static void tick(Level level, BlockPos blockPos, BlockState state, ReactorNeutronCollectorEntity entity) {
        if (level.isClientSide) {
            return;
        }
        CtiPacketHandler.sendToClient(new PMachineEnergySync(entity.ENERGY_STORAGE.getEnergyStored(), entity.getBlockPos()));
        if (!state.is(CtiBlock.reactor_neutron_collector.get())) {
            return;
        }
        for (Direction direction : List.of(Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH)) {
            if (entity.ENERGY_STORAGE.getEnergyStored() > 0) {
                BlockEntity energyContainer = level.getBlockEntity(entity.getBlockPos().relative(direction));
                if (energyContainer != null && energyContainer.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).isPresent()) {
                    IEnergyStorage storage = energyContainer.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).orElse(null);
                    int amount = Math.min(storage.receiveEnergy(entity.ENERGY_STORAGE.getEnergyStored(), true), entity.ENERGY_STORAGE.extractEnergy(entity.ENERGY_STORAGE.getEnergyStored(), true));
                    if (storage.receiveEnergy(amount, true) == amount) {
                        storage.receiveEnergy(amount, false);
                        entity.ENERGY_STORAGE.extractEnergy(amount, false);
                    }
                }
            } else break;
        }

        boolean good = level.dimension().equals(DimensionConstants.IONIZED_GLACIO);

        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockEntity inputContainer = level.getBlockEntity(blockPos.relative(direction.getClockWise()));
        BlockEntity outputContainer = level.getBlockEntity(blockPos.relative(direction.getCounterClockWise()));
        BlockEntity itemContainer = level.getBlockEntity(blockPos.above());
        if (itemContainer == null || outputContainer == null || inputContainer == null) {
            return;
        }
        LazyOptional<IGasHandler> inputOptional = inputContainer.getCapability(Capabilities.GAS_HANDLER, direction.getCounterClockWise());
        LazyOptional<IGasHandler> outputOptional = outputContainer.getCapability(Capabilities.GAS_HANDLER, direction.getClockWise());
        LazyOptional<IItemHandler> itemOptional = itemContainer.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN);
        IGasHandler inputHandler = inputOptional.orElse(EmptyHandlers.GAS_EMPTY);
        IGasHandler outputHandler = outputOptional.orElse(EmptyHandlers.GAS_EMPTY);
        IItemHandler itemHandler = itemOptional.orElse(EmptyHandlers.ITEM_EMPTY);
        if (inputHandler.equals(EmptyHandlers.GAS_EMPTY) || outputHandler.equals(EmptyHandlers.GAS_EMPTY) || itemHandler.equals(EmptyHandlers.ITEM_EMPTY)) {
            return;
        }
        Gas heatedSodium = MekanismGases.SUPERHEATED_SODIUM.get();
        Gas sodium = MekanismGases.SODIUM.get();

        boolean canInput = false;
        boolean canOutput = false;
        boolean canOutputItem = false;
        float SodiumAmplifier = 1;
        float chanceConsume = 0;
        ItemStack catalyst = entity.itemStackHandler.getStackInSlot(0);
        ReactorNeutronCollectorRecipe recipe = getRecipe(level, catalyst);
        ItemStack output = new ItemStack(ModItems.neutron_nugget.get());
        if (recipe != null) {
            SodiumAmplifier += recipe.getEfficiency() * (float) (catalyst.getCount() / recipe.getCatalyst().getCount());
            chanceConsume = recipe.getConsumptionRate() * (float) (catalyst.getCount() / recipe.getCatalyst().getCount());
            output = recipe.getResultItem();
        }
        if (good) {
            SodiumAmplifier *= 2f;
            chanceConsume *= 0.75f;
        }
        GasStack drain = GasStack.EMPTY;
        GasStack insert = GasStack.EMPTY;
        for (int a = 0; a < inputHandler.getTanks(); a++) {
            GasStack stack = inputHandler.getChemicalInTank(a);
            if (stack.getType() == heatedSodium) {
                drain = new GasStack(heatedSodium, (long) Math.min(entity.BASE_SODIUM_PERTICK * SodiumAmplifier, stack.getAmount()));
                GasStack gasStack = inputHandler.extractChemical(drain, Action.SIMULATE);
                if (gasStack.getAmount() > 0) {
                    drain = gasStack;
                    canInput = true;
                    break;
                } else {
                    drain = GasStack.EMPTY;
                }
            }
        }
        for (int a = 0; a < outputHandler.getTanks(); a++) {
            if (drain.isEmpty()) {
                break;
            }
            GasStack stack = outputHandler.getChemicalInTank(a);
            if (stack.getType() == sodium || stack.isEmpty()) {
                insert = new GasStack(sodium, Math.min(drain.getAmount(), outputHandler.getTankCapacity(a) - stack.getAmount()));
                GasStack gasStack = inputHandler.insertChemical(insert, Action.SIMULATE);
                if (gasStack.getAmount() > 0) {
                    insert = gasStack;
                    canOutput = true;
                    break;
                } else {
                    insert = GasStack.EMPTY;
                }
            }
        }
        if (!canInput || !canOutput) {
            return;
        }
        long amount = Math.min(drain.getAmount(), insert.getAmount());
        drain.setAmount(amount);
        insert.setAmount(amount);
        float fe = Math.min(Integer.MAX_VALUE, (float) amount / (float) entity.BASE_SODIUM_PERTICK);
        inputHandler.extractChemical(drain, Action.EXECUTE);
        outputHandler.insertChemical(insert, Action.EXECUTE);
        if (entity.PROGRESS < 2000000000) {
            entity.ENERGY_STORAGE.receiveEnergy((int) (entity.getEnergyPerTick() * fe), false);
            entity.PROGRESS = (int) Math.min(Integer.MAX_VALUE, entity.PROGRESS + amount);
            if (EtSHrnd().nextFloat() <= chanceConsume) {
                entity.itemStackHandler.extractItem(0, 1, false);
            }
            entity.setChanged();
        }
        if (entity.PROGRESS >= entity.MAX_PROGRESS) {
            for (int a = 0; a < itemHandler.getSlots(); a++) {
                if (itemHandler.getStackInSlot(a).isEmpty() && itemHandler.isItemValid(a, output)) {
                    while (entity.PROGRESS >= entity.MAX_PROGRESS && itemHandler.getStackInSlot(a).getCount() < itemHandler.getSlotLimit(a) && itemHandler.isItemValid(a, output)) {
                        itemHandler.insertItem(a, output, false);
                        entity.PROGRESS -= entity.MAX_PROGRESS;
                    }
                    break;
                } else if (itemHandler.getStackInSlot(a).is(output.getItem()) && itemHandler.getStackInSlot(a).getCount() < itemHandler.getSlotLimit(a)) {
                    while (entity.PROGRESS >= entity.MAX_PROGRESS && itemHandler.getStackInSlot(a).getCount() < itemHandler.getSlotLimit(a) && itemHandler.isItemValid(a, output)) {
                        itemHandler.insertItem(a, output, false);
                        entity.PROGRESS -= entity.MAX_PROGRESS;
                    }
                    break;
                }
            }
        }

    }

    public static ReactorNeutronCollectorRecipe getRecipe(Level level, ItemStack stack) {
        List<ReactorNeutronCollectorRecipe> list = List.copyOf(RecipeMap.NeutronRecipeList);
        for (ReactorNeutronCollectorRecipe recipe : list) {
            if (recipe.getCatalyst().is(stack.getItem())) {
                return recipe;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        CtiPacketHandler.sendToClient(new PMachineEnergySync(this.ENERGY_STORAGE.getEnergyStored(), this.getBlockPos()));
        return new ReactorNeutronCollectorMenu(i, inventory, this, this.DATA);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemStackHandler;
    }
}
