package com.hoshino.cti.mixin;

import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
    @Inject(method = "spawnFire",at = @At("HEAD"),cancellable = true)
    private void cancleFire(int pExtraIgnitions, CallbackInfo ci){
        LightningBolt bolt = (LightningBolt) (Object) this;
        if (bolt.getCause()!=null) ci.cancel();
    }
}
