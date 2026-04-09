package com.hoshino.cti.integration.botania.modifiers.base;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.BurstHitModifierHook;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.hook.UpdateBurstModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;

public class BasicBurstModifier extends EtSTBaseModifier implements ModifyBurstModifierHook, BurstHitModifierHook, UpdateBurstModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiBotModifierHooks.MODIFY_BURST,CtiBotModifierHooks.BURST_HIT,CtiBotModifierHooks.UPDATE_BURST);
    }
}
