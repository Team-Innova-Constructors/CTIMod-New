package com.hoshino.cti.Modifier.Armor;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Magical extends NoLevelsModifier implements ModifyDamageModifierHook , OnAttackedModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT,ModifierHooks.ON_ATTACKED);
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(damageSource.isBypassMagic())return v;
        if(damageSource.isMagic()){
            return v * 0.72f;
        }
        return v * 0.88f ;
    }

    @Override
    public void onAttacked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        var entity=equipmentContext.getEntity();
        boolean c=entity.getRandom().nextBoolean();
        var enemy=damageSource.getEntity();
        if(!(enemy instanceof Mob mob))return;
        if(c){
            mob.forceAddEffect(new MobEffectInstance(LCEffects.STONE_CAGE.get(),60,0),entity);
        }
        else mob.setDeltaMovement(new Vec3(0,2.5f,0));
    }
}
