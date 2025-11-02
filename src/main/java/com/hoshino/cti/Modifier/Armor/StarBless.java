package com.hoshino.cti.Modifier.Armor;

import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class StarBless extends Modifier implements DamageBlockModifierHook , ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK,ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public boolean isDamageBlocked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v){
        if(damageSource.getEntity() == null||damageSource.isMagic())return true;
        if(damageSource.getEntity()==equipmentContext.getEntity())return true;
        var attacker=damageSource.getEntity();
        var owner=equipmentContext.getEntity();
        if(!(attacker instanceof LivingEntity living))return false;
        int level=DifficultyLevel.ofAny(living);
        return level < owner.getArmorValue() + owner.getMaxHealth();
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        var entity=equipmentContext.getEntity();
        return Math.min(v, entity.getMaxHealth() * 0.2f);
    }
}
