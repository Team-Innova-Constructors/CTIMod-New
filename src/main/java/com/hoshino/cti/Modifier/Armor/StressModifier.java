package com.hoshino.cti.Modifier.Armor;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class StressModifier extends EtSTBaseModifier {
    @Override
    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        super.modifierOnAttacked(tool, modifier, context, slotType, source, amount, isDirectDamage);
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player && amount > 1) {
            MobEffectInstance instance = player.getEffect(CtiEffects.stress.get());
            player.addEffect(new MobEffectInstance(CtiEffects.stress.get(), 200, Math.min(9, (instance == null ? 0 : instance.getAmplifier()) + modifier.getLevel()), false, false));
        }
    }
}
