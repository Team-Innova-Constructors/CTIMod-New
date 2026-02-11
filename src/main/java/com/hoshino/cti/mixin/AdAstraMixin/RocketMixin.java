package com.hoshino.cti.mixin.AdAstraMixin;

import earth.terrarium.ad_astra.common.entity.vehicle.Rocket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Rocket.class,remap = false)
public class RocketMixin {
    @Unique private boolean cti$didDropped = false;

    @Inject(method = "drop",cancellable = true,at = @At("HEAD"))
    public void cancelExtraDrop(CallbackInfo ci){
        if (cti$didDropped) ci.cancel();
        else cti$didDropped = true;
    }
}
