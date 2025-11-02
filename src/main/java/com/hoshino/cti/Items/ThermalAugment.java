package com.hoshino.cti.Items;

import cofh.core.item.IAugmentItem;
import cofh.core.item.ItemCoFH;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ThermalAugment extends ItemCoFH implements IAugmentItem {

    private CompoundTag augmentData;

    public ThermalAugment(Properties builder, CompoundTag augmentData) {

        super(builder);
        setAugmentData(augmentData);
    }

    public boolean setAugmentData(CompoundTag augmentData) {

        if (augmentData == null || augmentData.isEmpty()) {
            return false;
        }
        this.augmentData = augmentData;
        return true;
    }

    @Nullable
    @Override
    public CompoundTag getAugmentData(ItemStack augment) {

        return augmentData;
    }
}
