package com.hoshino.cti.mixin.AdAstraMixin;

import static com.hoshino.cti.content.environmentSystem.EnvironmentalHandler.*;
import earth.terrarium.ad_astra.common.entity.system.EntityTemperatureSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityTemperatureSystem.class, remap = false)
public abstract class TemperatureTick {
    @Inject(method = "burnEntity", at = @At("HEAD"), cancellable = true)
    private static void burnEntity(LivingEntity entity, CallbackInfo callbackInfo) {
        if (getScorchResistance(entity) >= 1) {
            callbackInfo.cancel();
        }

    }

    @Inject(method = "freezeEntity", at = @At("HEAD"), cancellable = true)
    private static void freezeEntity(LivingEntity entity, ServerLevel level, CallbackInfo callbackInfo) {
        if (getFrozenResistance(entity) >= 1) {
            callbackInfo.cancel();
        }

    }
}
