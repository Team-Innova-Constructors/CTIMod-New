package com.hoshino.cti.util.mixin.create;

import net.minecraft.world.item.ItemStack;

public interface IDeployerBlockEntityMixin {
    void cti$setHeldItem(ItemStack stack);
    ItemStack cti$getHeldItem();
}
