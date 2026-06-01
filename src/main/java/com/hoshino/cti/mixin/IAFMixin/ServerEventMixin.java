package com.hoshino.cti.mixin.IAFMixin;

import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraftforge.event.village.VillagerTradesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerEvents.class,remap = false)
public class ServerEventMixin {
    @Inject(method = "onVillagerTrades",at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/iceandfire/entity/IafVillagerRegistry;addScribeTrades(Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;)V"), cancellable = true)
    private void prevent(VillagerTradesEvent event, CallbackInfo ci){
        ci.cancel();
    }
}
