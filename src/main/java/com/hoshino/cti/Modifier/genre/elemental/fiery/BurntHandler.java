package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;

public class BurntHandler extends Modifier implements AttributesModifierHook {
    @Override
    public int getPriority() {
        return 999;
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES);
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer){
        if (slot==EquipmentSlot.OFFHAND&&!tool.hasTag(TinkerTags.Items.SHIELDS)) return;
        consumer.accept(CtiAttributes.BURNT_INFLICT.get(),new AttributeModifier(
                CommonUtil.UUIDFromSlot(slot,modifier.getId()),CtiAttributes.BURNT_INFLICT.get().getDescriptionId(),
                tool.getStats().get(GenreManager.BURNT_GENRE.baseStat), AttributeModifier.Operation.ADDITION
        ));
        consumer.accept(CtiAttributes.BURNT_ATTACK_SPEED.get(),new AttributeModifier(
                CommonUtil.UUIDFromSlot(slot,modifier.getId()),CtiAttributes.BURNT_ATTACK_SPEED.get().getDescriptionId(),
                tool.getStats().get(GenreManager.BURNT_GENRE.mulStat), AttributeModifier.Operation.ADDITION
        ));
    }
}
