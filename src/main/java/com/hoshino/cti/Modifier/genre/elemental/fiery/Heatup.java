package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;

public class Heatup extends BasicBurntModifier implements AttributesModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.ATTRIBUTES);
    }

    @Override
    public int getReleasedBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 20;
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 80;
    }


    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(CtiAttributes.BURNT_INFLICT.get(),
                new AttributeModifier(CommonUtil.UUIDFromSlot(slot,modifier.getId()),"desc.cti.heatup",0.2*modifier.getLevel(), AttributeModifier.Operation.MULTIPLY_BASE));
    }
}
