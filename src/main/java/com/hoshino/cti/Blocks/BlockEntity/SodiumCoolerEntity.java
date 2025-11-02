package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.Blocks.Machine.SodiumCooler;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiBlockEntityType;
import com.hoshino.cti.util.EmptyHandlers;
import com.hoshino.cti.util.ctiEnergyStore;
import mekanism.api.Action;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.registries.MekanismGases;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SodiumCoolerEntity extends GeneralMachineEntity {
    public SodiumCoolerEntity(BlockPos blockPos, BlockState blockState) {
        super(CtiBlockEntityType.SODIUM_COOLER.get(), blockPos, blockState);
    }

    protected int MAX_ENERGY = 2000000000;
    protected int MAX_TRANSFER = 2000000000;
    protected int BASE_ENERGY_PERTICK = 50000000;


    public final ctiEnergyStore ENERGY_STORAGE = new ctiEnergyStore(getMaxEnergy(), 0, getMaxTransfer()) {
        @Override
        public void onEnergyChange() {
            setChanged();
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

    public int getEnergyPerTick() {
        return this.BASE_ENERGY_PERTICK;
    }


    private LazyOptional<IEnergyStorage> LazyenergyHandler = LazyOptional.empty();
    private LazyOptional<IGasHandler> LazyGasHandler = LazyOptional.empty();


    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == ForgeCapabilities.ENERGY) {
            return LazyenergyHandler.cast();
        }
        Direction locDir = this.getBlockState().getValue(SodiumCooler.FACING);
        if (capability == Capabilities.GAS_HANDLER && (direction == locDir.getClockWise() || direction == locDir.getCounterClockWise())) {
            return LazyGasHandler.cast();
        }
        return super.getCapability(capability, direction);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        LazyenergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
        LazyGasHandler = LazyOptional.of(() -> gasHandler);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return null;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        LazyenergyHandler.invalidate();
        LazyGasHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("energy_store", ENERGY_STORAGE.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        ENERGY_STORAGE.deserializeNBT(nbt.get("energy_store"));
        super.load(nbt);
    }

    @Nullable
    public ctiEnergyStore getEnergyStorage() {
        return ENERGY_STORAGE;
    }


    public static void tick(Level level, BlockPos blockPos, BlockState state, SodiumCoolerEntity entity) {
        if (level.isClientSide) {
            return;
        }
        if (!state.is(CtiBlock.sodium_cooler_block.get())) {
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
        if (entity.ENERGY_STORAGE.getEnergyStored() > entity.ENERGY_STORAGE.getMaxEnergyStored() - entity.getEnergyPerTick()) {
            return;
        }
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BlockEntity inputContainer = level.getBlockEntity(blockPos.relative(direction.getClockWise()));
        BlockEntity outputContainer = level.getBlockEntity(blockPos.relative(direction.getCounterClockWise()));
        if (outputContainer == null || inputContainer == null) {
            return;
        }
        LazyOptional<IGasHandler> inputOptional = inputContainer.getCapability(Capabilities.GAS_HANDLER, direction.getCounterClockWise());
        LazyOptional<IGasHandler> outputOptional = outputContainer.getCapability(Capabilities.GAS_HANDLER, direction.getClockWise());
        IGasHandler inputHandler = inputOptional.orElse(EmptyHandlers.GAS_EMPTY);
        IGasHandler outputHandler = outputOptional.orElse(EmptyHandlers.GAS_EMPTY);
        if (inputHandler.equals(EmptyHandlers.GAS_EMPTY) || outputHandler.equals(EmptyHandlers.GAS_EMPTY)) {
            return;
        }
        Gas Fuel = MekanismGases.FISSILE_FUEL.get();
        Gas Waste = MekanismGases.NUCLEAR_WASTE.get();
        boolean canInput = false;
        boolean canOutput = false;
        GasStack drain = GasStack.EMPTY;
        GasStack insert = GasStack.EMPTY;
        for (int a = 0; a < inputHandler.getTanks(); a++) {
            GasStack stack = inputHandler.getChemicalInTank(a);
            if (stack.getType() == Fuel) {
                drain = new GasStack(Fuel, Math.min(stack.getAmount(), 2870));
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
            if (stack.getType() == Waste || stack.isEmpty()) {
                insert = new GasStack(Waste, Math.min(drain.getAmount(), outputHandler.getTankCapacity(a) - stack.getAmount()));
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
        float multiplier = (float) amount / 2870f;
        drain.setAmount(amount);
        insert.setAmount(amount);
        inputHandler.extractChemical(drain, Action.EXECUTE);
        outputHandler.insertChemical(insert, Action.EXECUTE);
        entity.ENERGY_STORAGE.setEnergy(entity.ENERGY_STORAGE.getEnergy() + (int) (entity.getEnergyPerTick() * multiplier));
        entity.setChanged();
    }


}
