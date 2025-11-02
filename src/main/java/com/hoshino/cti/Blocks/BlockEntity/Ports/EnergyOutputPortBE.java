package com.hoshino.cti.Blocks.BlockEntity.Ports;

import com.hoshino.cti.util.ctiEnergyStore;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnergyOutputPortBE extends BasicPort{
    public @NotNull final ctiEnergyStore energyStorage;

    public EnergyOutputPortBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int energyStorage) {
        super(pType, pPos, pBlockState,PortType.ENERGY,TransferType.OUTPUT);
        this.energyStorage = new ctiEnergyStore(energyStorage,energyStorage) {
            @Override
            public void onEnergyChange() {
                setChanged();
            }

            @Override
            public boolean canReceive() {
                return false;
            }
        };
    }
    private LazyOptional<IEnergyStorage> energyOptional = LazyOptional.empty();
    @Override
    public void onLoad() {
        super.onLoad();
        this.energyOptional = LazyOptional.of(()->energyStorage);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.energyStorage.writeToNbt(pTag);
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.energyStorage.readFromNbt(pTag);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level == null || this.level.isClientSide) return;
        if (this.controllerPos!=null){
            BlockEntity controller = this.level.getBlockEntity(this.worldPosition);
            if (controller!=null){
                controller.getCapability(ForgeCapabilities.ENERGY).filter(IEnergyStorage::canExtract).ifPresent(energyHandler->{
                    int extract =Math.min( energyHandler.extractEnergy(energyHandler.getEnergyStored(),true),this.energyStorage.getMaxEnergyStored()-this.energyStorage.getEnergy());
                    if (extract>0){
                        energyHandler.extractEnergy(extract,false);
                        this.energyStorage.setEnergy(this.energyStorage.getEnergyStored()+extract);
                    }
                });
            }
        }
        for (Direction direction: List.of(Direction.UP,Direction.DOWN,Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST)){
            int energyToOutput = this.energyStorage.getEnergy();
            if (energyToOutput<=0) return;
            BlockPos relative = this.worldPosition.relative(direction);
            BlockEntity blockEntity = this.level.getBlockEntity(relative);
            if (blockEntity!=null){
                blockEntity.getCapability(ForgeCapabilities.ENERGY,direction.getOpposite()).filter(IEnergyStorage::canReceive).ifPresent(energyHandler->{
                    int insert = Math.min(this.energyStorage.extractEnergy(energyToOutput,true) ,energyHandler.receiveEnergy(energyToOutput,true));
                    if (insert>0){
                        energyHandler.receiveEnergy(insert,false);
                        this.energyStorage.extractEnergy(insert,false);
                    }
                });
            }
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap==ForgeCapabilities.ENERGY) return this.energyOptional.cast();
        return super.getCapability(cap);
    }


}
