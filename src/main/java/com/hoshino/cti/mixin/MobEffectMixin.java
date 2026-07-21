package com.hoshino.cti.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.extensions.IForgeMobEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements IForgeMobEffect {
    @Shadow
    public abstract String getDescriptionId();


    @Inject(method = "getAttributeModifierValue", at = @At("HEAD"), cancellable = true)
    private void limitSpeedReduction(int pAmplifier, AttributeModifier pModifier, CallbackInfoReturnable<Double> cir) {
        MobEffect effect = (MobEffect) (Object) this;
        if (effect == MobEffects.MOVEMENT_SLOWDOWN) {
            double baseAmount = pModifier.getAmount();
            double calculated = baseAmount * (double) (pAmplifier + 1);
            if (calculated < -0.45D) {
                cir.setReturnValue(-0.45D);
            }}
            }
}
