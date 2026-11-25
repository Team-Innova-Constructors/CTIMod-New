package com.hoshino.cti.mixin.TwiMixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.KnightPhantom;

@Mixin(value = KnightPhantom.class)
public class KnightPhantomMixin {
    @Inject(at = @At("HEAD"),cancellable = true,method = "knockback")
    private void cancelKnockBack(double damage, double xRatio, double zRatio, CallbackInfo ci){
        ci.cancel();
    }
}
