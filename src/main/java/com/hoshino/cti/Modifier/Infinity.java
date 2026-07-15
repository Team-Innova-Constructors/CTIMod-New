package com.hoshino.cti.Modifier;

import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.EntityUtil;
import com.hoshino.cti.util.ILivingEntityMixin;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class Infinity extends Modifier implements ToolStatsModifierHook, MeleeDamageModifierHook, MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.SCORCH_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.FROZEN_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.PRESSURE_RESISTANCE.add(modifierStatsBuilder, 50);
    }


    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float baseDamage, float damage) {
        var livingTarget = toolAttackContext.getLivingTarget();
        var attacker = toolAttackContext.getAttacker();
        var level = modifierEntry.getLevel();
        if (livingTarget instanceof Mob mob && attacker instanceof Player player) {
            mob.invulnerableTime = 0;
            if(level<=2){
                mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(), 131072);
                return damage + 131072;
            }
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var livingTarget = context.getLivingTarget();
        var attacker = context.getAttacker();
        var level = modifier.getLevel();
        if (!(livingTarget instanceof Mob mob)) return;
        if (!(attacker instanceof Player player)) return;
        if (level >= 3) {
            EntityUtil.constantKill(mob,DamageSource.playerAttack(player));
        }
    }
}
