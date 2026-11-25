package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.marth7th.solidarytinker.register.SolidarytinkerToolstats;
import com.xiaoyue.tinkers_ingenuity.content.library.tools.TIToolStats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class UnacceptableWeight extends NoLevelsModifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.ATTACK_SPEED.multiply(builder,0.2);
        SolidarytinkerToolstats.ATTACK_FREQUENCY.percent(builder,25);
        TIToolStats.COOLDOWN.multiply(builder,5);
        etshtinkerToolStats.COOLDOWN.multiply(builder,5);
        ToolStats.ATTACK_DAMAGE.multiply(builder,10);
    }
}
