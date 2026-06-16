package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.armor.Reactive;
import com.xiaoyue.tinkers_ingenuity.register.TIEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = Reactive.class,remap = false)
public class ReactiveMixin {
    @Inject(method = "onModifyTakeDamage",at = @At("RETURN"), cancellable = true)
    private void set(IToolStackView tool, int level, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean isDirectDamage, CallbackInfoReturnable<Float> cir){
        LivingEntity entity = context.getEntity();
        int effectLevel = (TIEffects.REACTIVE.get()).getLevel(entity);
        cir.setReturnValue(amount * (1.0F - 0.02F * (float)effectLevel));
    }
}
