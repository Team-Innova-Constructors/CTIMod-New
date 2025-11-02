package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modules.armor.KineticModule;
import slimeknights.tconstruct.tools.stats.ToolType;

@Mixin(value = KineticModule.class, remap = false)
public class KineticMixin {
    @Redirect(method = "onAttacked", remap = false, at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/tools/modifiers/traits/melee/InsatiableModifier;applyEffect(Lnet/minecraft/world/entity/LivingEntity;Lslimeknights/tconstruct/tools/stats/ToolType;III)V"))
    private void applyEffect(LivingEntity living, ToolType type, int duration, int add, int maxLevel) {
        TinkerEffect effect = TinkerModifiers.insatiableEffect.get(type);
        effect.apply(living, duration * 4, Math.min(maxLevel * 9, effect.getLevel(living) + add * 4), true);
    }
}
