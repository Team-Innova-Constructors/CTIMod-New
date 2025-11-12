package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.traits.legendary.KillerAuraTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KillerAuraTrait.class)
public class KillHaloMixin {
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;m_6469_(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true,remap = false)
    private void tick(LivingEntity mob, int level, CallbackInfo ci){
        if(mob instanceof Mob m){
            if(m.getTarget()instanceof Player player){
                if(GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                    ci.cancel();
                }
            }
        }
    }
}
