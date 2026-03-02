package com.hoshino.cti.Modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;
import java.util.function.BiConsumer;

public class AluminiumPower extends Modifier implements ModifyDamageModifierHook , AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT,ModifierHooks.ATTRIBUTES);
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(damageSource.isExplosion()){
             v= v*0.1f;
        }
        else if(damageSource.isProjectile()||damageSource.isMagic()||damageSource.isFall()||damageSource.isFire()){
            v=v * 0.4f;
        }
        if(damageSource.getEntity()!=null){
            return v-2;
        }
        return v;
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        var speedAttribute =new AttributeModifier(UUID.nameUUIDFromBytes((this.getId()+equipmentSlot.getName()).getBytes()), Attributes.MAX_HEALTH.getDescriptionId(),0.05, AttributeModifier.Operation.ADDITION);
        biConsumer.accept(Attributes.MOVEMENT_SPEED, speedAttribute);
    }
}
