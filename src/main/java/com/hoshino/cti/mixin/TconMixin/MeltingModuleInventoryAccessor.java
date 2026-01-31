package com.hoshino.cti.mixin.TconMixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;

@Mixin(value = MeltingModuleInventory.class,remap = false)
public interface MeltingModuleInventoryAccessor {
    @Accessor("modules")
    MeltingModule[] cti$getModules();
}
