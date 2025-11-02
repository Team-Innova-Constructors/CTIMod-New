package com.hoshino.cti.Blocks.BlockEntity;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class WrappedHandler implements IItemHandlerModifiable {
    public final IItemHandlerModifiable itemHandlerModifiable;
    public final Predicate<Integer> extract;
    public final BiPredicate<Integer, ItemStack> insert;

    public WrappedHandler(IItemHandlerModifiable itemHandlerModifiable, Predicate<Integer> extract, BiPredicate<Integer, ItemStack> insert) {
        this.itemHandlerModifiable = itemHandlerModifiable;
        this.extract = extract;
        this.insert = insert;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack itemStack) {
        this.itemHandlerModifiable.setStackInSlot(slot, itemStack);
    }

    @Override
    public int getSlots() {
        return this.itemHandlerModifiable.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return this.itemHandlerModifiable.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack itemStack, boolean simulate) {
        return this.insert.test(slot, itemStack) ? this.itemHandlerModifiable.insertItem(slot, itemStack, simulate) : itemStack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.extract.test(slot) ? this.itemHandlerModifiable.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.itemHandlerModifiable.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack itemStack) {
        return this.insert.test(slot, itemStack) && this.itemHandlerModifiable.isItemValid(slot, itemStack);
    }
}
