package com.hoshino.cti.mixin.COEMixin;

import com.tom.createores.OreDataCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OreDataCapability.OreData.class)
public interface OreDataAccessor {
    @Accessor(remap = false)
    long getExtractedAmount();
    @Accessor(remap = false)
    float getRandomMul();
}
