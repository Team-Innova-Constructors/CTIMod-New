package com.hoshino.cti.Modifier;

import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MandarinDuck extends Modifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int m = context.getModifier(CtiModifiers.THE_PAST_STATIC_MODIFIER.getId()).getLevel() > 0 ? 4 : 2;
        float value = modifier.getLevel() * m;
        ToolStats.ARMOR.add(builder, value);
        ToolStats.ARMOR_TOUGHNESS.add(builder, value);
    }
}
