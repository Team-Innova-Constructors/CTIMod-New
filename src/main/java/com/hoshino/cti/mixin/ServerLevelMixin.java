package com.hoshino.cti.mixin;

import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "tickNonPassenger",at = @At(value = "HEAD"),cancellable = true)
    public void tickEntityTicker(Entity entity, CallbackInfo ci){
        if(!EntityTickerManager.tick(entity)) ci.cancel();
    }
}
