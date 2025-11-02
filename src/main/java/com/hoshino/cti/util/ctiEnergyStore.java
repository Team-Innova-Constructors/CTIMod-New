package com.hoshino.cti.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;

public abstract class ctiEnergyStore extends EnergyStorage {
    public ctiEnergyStore(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer);
    }

    public ctiEnergyStore(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractEnergy != 0) {
            onEnergyChange();
        }
        return extractEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if (receiveEnergy != 0) {
            onEnergyChange();
        }
        return receiveEnergy;
    }

    public void setEnergy(int amount) {
        this.energy = amount;
    }

    public void setMaxEnergy(int amount) {
        this.capacity = amount;
    }

    public int getEnergy() {
        return this.energy;
    }

    public abstract void onEnergyChange();

    public void writeToNbt(CompoundTag tag){
        tag.putInt("energy",this.energy);
    }
    public void readFromNbt(CompoundTag tag){
        this.energy=tag.getInt("energy");
    }
}
