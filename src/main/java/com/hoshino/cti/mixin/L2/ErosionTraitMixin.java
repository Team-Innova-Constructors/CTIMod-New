package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.highlevel.CorrosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.ErosionTrait;
import dev.xkmc.l2hostility.content.traits.highlevel.SlotIterateDamageTrait;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ErosionTrait.class,remap = false)
public abstract class ErosionTraitMixin extends SlotIterateDamageTrait {
    public ErosionTraitMixin(ChatFormatting format) {
        super(format);
    }
    @Inject(method = "onHurtTarget",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/backport/damage/DamageModifier;hurtMultTotal(Ldev/xkmc/l2library/init/events/attack/AttackCache;F)V"), cancellable = true)
    private void cancel(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache, CallbackInfo ci){
        ci.cancel();
    }
}
