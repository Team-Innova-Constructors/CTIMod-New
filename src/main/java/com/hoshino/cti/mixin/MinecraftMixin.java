package com.hoshino.cti.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Unique
    private int attack$waitTime =0;
    @Inject(
            method = "startAttack",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void enforceDelayAfterEntityAttack(CallbackInfoReturnable<Boolean> cir) {
        if(attack$waitTime !=0){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "startAttack",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onClickInput(ILnet/minecraft/client/KeyMapping;Lnet/minecraft/world/InteractionHand;)Lnet/minecraftforge/client/event/InputEvent$InteractionKeyMappingTriggered;",remap = false))
    private void c(CallbackInfoReturnable<Boolean> cir){
        attack$waitTime=2;
    }
    @Inject(method = "tick",at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        if(attack$waitTime>0){
            attack$waitTime--;
        }
    }
}
