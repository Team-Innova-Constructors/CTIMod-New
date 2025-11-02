package com.hoshino.cti.Modifier;

import com.hoshino.cti.register.CtiToolStats;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class Infinity extends BattleModifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.SCORCH_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.FROZEN_RESISTANCE.add(modifierStatsBuilder, 50);
        CtiToolStats.PRESSURE_RESISTANCE.add(modifierStatsBuilder, 50);
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (livingTarget instanceof Mob mob && attacker instanceof Player player) {
            mob.invulnerableTime=0;
            switch (level){
                case 1, 2 -> {
                    mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(),131072);
                    return damage + 131072;
                }
                default -> {
                    mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(),Float.MAX_VALUE);
                    return Float.MAX_VALUE;
                }
            }
        }
        return damage;
    }
    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (target instanceof Mob mob&& attacker instanceof Player player) {
            mob.invulnerableTime=0;
            switch (level){
                case 1, 2 -> {
                    mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(),131072);
                    arrow.setBaseDamage(131072 * level);
                }
                default -> {
                    mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(),Float.MAX_VALUE);
                    arrow.setBaseDamage(Integer.MAX_VALUE);
                }
            }
        }
    }
}
