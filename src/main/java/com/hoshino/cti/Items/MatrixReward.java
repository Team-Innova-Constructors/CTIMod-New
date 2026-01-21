package com.hoshino.cti.Items;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record MatrixReward(Item item, int minAmount, int randomAdd) {
    public ItemStack roll(RandomSource random) {
        int amount = minAmount + (randomAdd > 0 ? random.nextInt(randomAdd + 1) : 0);
        return new ItemStack(item, amount);
    }
}
