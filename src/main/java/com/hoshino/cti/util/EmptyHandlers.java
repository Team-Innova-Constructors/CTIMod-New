package com.hoshino.cti.util;

import mekanism.api.Action;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class EmptyHandlers {
    public static final IGasHandler GAS_EMPTY = new IGasHandler() {
        @Override
        public int getTanks() {
            return 0;
        }

        @Override
        public GasStack getChemicalInTank(int i) {
            return null;
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
            return null;
        }

        @Override
        public GasStack extractChemical(int i, long l, Action action) {
            return null;
        }
    };

    public static final IItemHandler ITEM_EMPTY = new IItemHandler() {
        @Override
        public int getSlots() {
            return 0;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int i) {
            return null;
        }

        @Override
        public @NotNull ItemStack insertItem(int i, @NotNull ItemStack itemStack, boolean b) {
            return null;
        }

        @Override
        public @NotNull ItemStack extractItem(int i, int i1, boolean b) {
            return null;
        }

        @Override
        public int getSlotLimit(int i) {
            return 0;
        }

        @Override
        public boolean isItemValid(int i, @NotNull ItemStack itemStack) {
            return false;
        }
    };
}
