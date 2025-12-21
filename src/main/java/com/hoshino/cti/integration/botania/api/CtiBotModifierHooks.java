package com.hoshino.cti.integration.botania.api;

import com.hoshino.cti.Cti;
import com.hoshino.cti.integration.botania.api.hook.*;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHook;

public class CtiBotModifierHooks {
    public static final ModuleHook<UpdateBurstModifierHook> UPDATE_BURST = ModifierHooks.register(Cti.getResource("update_burst"), UpdateBurstModifierHook.class, UpdateBurstModifierHook.AllMerger::new, new UpdateBurstModifierHook() {});
    public static final ModuleHook<ModifyBurstModifierHook> MODIFY_BURST = ModifierHooks.register(Cti.getResource("modify_burst"), ModifyBurstModifierHook.class, ModifyBurstModifierHook.AllMerger::new, new ModifyBurstModifierHook() {});
    public static final ModuleHook<BurstDamageModifierHook> BURST_DAMAGE = ModifierHooks.register(Cti.getResource("burst_damage"), BurstDamageModifierHook.class, BurstDamageModifierHook.AllMerger::new, (tool, modifier, modifierList, owner, target, burst, burstExtra, baseDamage, damage) -> damage);
    public static final ModuleHook<BurstHitModifierHook> BURST_HIT = ModifierHooks.register(Cti.getResource("burst_hit"), BurstHitModifierHook.class,BurstHitModifierHook.merger::new, new BurstHitModifierHook() {});
    public static final ModuleHook<LensProviderModifierHook> LENS_PROVIDER = ModifierHooks.register(Cti.getResource("lens_provider"), LensProviderModifierHook.class,LensProviderModifierHook.AllMerger::new, new LensProviderModifierHook() {});
}
