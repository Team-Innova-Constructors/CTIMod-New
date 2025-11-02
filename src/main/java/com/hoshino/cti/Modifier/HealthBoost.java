package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;
import java.util.function.BiConsumer;

public class HealthBoost extends ArmorModifier {
    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        var healthAttribute=new AttributeModifier(UUID.nameUUIDFromBytes((this.getId()+slot.getName()).getBytes()), Attributes.MAX_HEALTH.getDescriptionId(),5, AttributeModifier.Operation.ADDITION);
        consumer.accept(Attributes.MAX_HEALTH,healthAttribute);
    }
}
