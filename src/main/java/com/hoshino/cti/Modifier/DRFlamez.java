package com.hoshino.cti.Modifier;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class DRFlamez extends NoLevelsModifier implements OnAttackedModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED);
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        var attacker=source.getEntity();
        if(!(attacker instanceof LivingEntity living))return;
        if(source instanceof EntityDamageSource entityDamageSource&&entityDamageSource.isThorns())return;
        boolean hasFire=living.hasEffect(LCEffects.FLAME.get());
        if(!hasFire){
            living.forceAddEffect(new MobEffectInstance(LCEffects.FLAME.get(),20 * 118,5),context.getEntity());
        }
        if(attacker.getRemainingFireTicks()<=118 * 20){
            attacker.setRemainingFireTicks(attacker.getRemainingFireTicks() + 118);
        }
    }
}
