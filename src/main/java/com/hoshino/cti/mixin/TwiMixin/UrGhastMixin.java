package com.hoshino.cti.mixin.TwiMixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import twilightforest.entity.boss.UrGhast;

@Mixin(UrGhast.class)
public class UrGhastMixin {
    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Ltwilightforest/entity/boss/UrGhast;getHealth()F", ordinal = 0), index = 2, argsOnly = true)
    private float changeParam(float value){
        return value * 5;
    }
}
