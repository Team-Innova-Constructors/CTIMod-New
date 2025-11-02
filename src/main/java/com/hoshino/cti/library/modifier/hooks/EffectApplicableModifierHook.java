package com.hoshino.cti.library.modifier.hooks;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface EffectApplicableModifierHook {
    boolean isNotApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, boolean notApplicable);
    record AllMerger(Collection<EffectApplicableModifierHook> modules) implements EffectApplicableModifierHook {
        @Override
        public boolean isNotApplicable(IToolStackView tool, ModifierEntry entry, EquipmentSlot slot, MobEffectInstance instance, boolean notApplicable) {
            for (EffectApplicableModifierHook module:this.modules){
                boolean NotApplicable =module.isNotApplicable(tool,entry,slot,instance,notApplicable);
                if (NotApplicable) return true;
            }
            return notApplicable;
        }
    }
}
