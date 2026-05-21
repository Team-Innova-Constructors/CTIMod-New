package com.hoshino.cti.Modifier;

import com.hoshino.cti.util.CommonUtil;
import com.marth7th.solidarytinker.util.MathUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;

public class Ethereal extends Modifier implements ModifyDamageModifierHook ,DamageBlockModifierHook, AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT,ModifierHooks.DAMAGE_BLOCK,ModifierHooks.ATTRIBUTES);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(source.isMagic()||source.isExplosion()){
            amount=amount*0.5f;
        }
        return amount;
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount) {
        var randomSource=context.getEntity().getRandom();
        return randomSource.nextFloat() * 100f<15f;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        var attModifier=new AttributeModifier(CommonUtil.UUIDFromSlot(slot,this.getId()), Attributes.MAX_HEALTH.getDescriptionId(), 40 * modifier.getLevel(), AttributeModifier.Operation.ADDITION);
        consumer.accept(Attributes.MAX_HEALTH,attModifier);
    }
}
