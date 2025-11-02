package com.hoshino.cti.library.modifier;

import com.hoshino.cti.Cti;
import com.hoshino.cti.library.modifier.hooks.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class CtiModifierHook {
    public static final ModuleHook<LeftClickModifierHook> LEFT_CLICK = ModifierHooks.register(Cti.getResource("left_click"), LeftClickModifierHook.class, LeftClickModifierHook.AllMerger::new, new LeftClickModifierHook() {});
    public static final ModuleHook<EffectApplicableModifierHook> EFFECT_APPLICABLE = ModifierHooks.register(Cti.getResource("effect_applicable"), EffectApplicableModifierHook.class, EffectApplicableModifierHook.AllMerger::new,(tool, entry, equipmentSlot, instance, applicable)->applicable);
    public static final ModuleHook<OnDeathModifierHook> ON_DEATH = ModifierHooks.register(Cti.getResource("on_death"), OnDeathModifierHook.class, OnDeathModifierHook.AllMerger::new, (tool, modifier, context, slotType, source, victim, isAliveSource) -> {});
    public static final ModuleHook<OnHoldingPreventDeathHook> PREVENT_DEATH = ModifierHooks.register(Cti.getResource("holding_death"), OnHoldingPreventDeathHook.class, OnHoldingPreventDeathHook.FirstMerger::new, new OnHoldingPreventDeathHook() {
        @Override
        public float onHoldingPreventDeath(LivingEntity livingEntity, IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source) {
            return 0;
        }

        @Override
        public boolean canIgnorePassInvul() {
            return false;
        }
    });
    public static final ModuleHook<SlotStackModifierHook> SLOT_STACK = ModifierHooks.register(Cti.getResource("slot_stack"), SlotStackModifierHook.class, SlotStackModifierHook.FirstMerger::new, new SlotStackModifierHook(){});
}
