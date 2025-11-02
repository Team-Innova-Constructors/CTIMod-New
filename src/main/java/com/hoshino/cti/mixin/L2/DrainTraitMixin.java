package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.marth7th.solidarytinker.register.solidarytinkerModifiers;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import dev.xkmc.l2hostility.content.traits.highlevel.DrainTrait;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DrainTrait.class, remap = false)
public class DrainTraitMixin {
    @Inject(at = {@At("HEAD")}, method = {"postHurtImpl"}, cancellable = true)
    private void RemoveEffect(int level, LivingEntity attacker, LivingEntity target, CallbackInfo ci) {
        if (ModifierLevel.EquipHasModifierlevel(target, solidarytinkerModifiers.CLEAN_STATIC_MODIFIER.getId())) {
            ci.cancel();
        }
        if(GetModifierLevel.CurioHasModifierlevel(target, TinkerCuriosModifier.CleanCurio.getId())||GetModifierLevel.CurioHasModifierlevel(target, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
            ci.cancel();
        }
    }
}
