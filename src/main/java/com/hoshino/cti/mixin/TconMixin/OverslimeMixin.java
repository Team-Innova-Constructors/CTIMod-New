package com.hoshino.cti.mixin.TconMixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

@Mixin(value = OverslimeModifier.class,remap = false)
public class OverslimeMixin {
    @ModifyConstant(method = "<clinit>",constant = @Constant(floatValue = Short.MAX_VALUE))
    @Unique
    private static float modifyOverslimeMaxValue(float constant) {
        return Integer.MAX_VALUE;
    }
}
