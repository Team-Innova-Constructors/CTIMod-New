package com.hoshino.cti.Modifier;

import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import com.marth7th.solidarytinker.register.solidarytinkerToolstats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class BreakThrough extends NoLevelsModifier implements MeleeHitModifierHook, ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.MELEE_HIT,ModifierHooks.TOOL_STATS);
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        EnvironmentalHandler.hurtEntity(context.getAttacker(),EDamageSource.selfDamage(1),0.075f*context.getAttacker().getMaxHealth());
        return knockback;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        solidarytinkerToolstats.ATTACK_FREQUENCY.percent(builder,-0.15);
        ToolStats.ATTACK_DAMAGE.percent(builder,0.1);
        ToolStats.ATTACK_SPEED.percent(builder,0.1);
    }
}
