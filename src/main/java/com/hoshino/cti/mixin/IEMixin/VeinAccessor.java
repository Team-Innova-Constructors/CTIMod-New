package com.hoshino.cti.mixin.IEMixin;

import blusunrize.immersiveengineering.api.excavator.MineralVein;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MineralVein.class)
public interface VeinAccessor {
    @Accessor(remap = false)
    ResourceLocation getMineralName();
}
