package com.hoshino.cti.Modifier;

import net.minecraft.world.damagesource.DamageSource;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SoulFlame extends NoLevelsModifier implements MeleeDamageModifierHook , MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.MELEE_HIT);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var target=context.getLivingTarget();
        var player=context.getPlayerAttacker();
        if(target==null||player==null)return damage;
        var burnTime=target.getRemainingFireTicks();
        if(burnTime==0||target.fireImmune())return damage;
        var finalScale=burnTime/20f;
        return damage+Math.min(player.getMaxHealth(),finalScale * 4f);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var target=context.getLivingTarget();
        var player=context.getPlayerAttacker();
        if(target==null||player==null)return;

        if(target.fireImmune()){
            float damageStat=tool.getStats().get(ToolStats.ATTACK_DAMAGE);
            target.invulnerableTime=0;
            target.hurt(DamageSource.indirectMagic(player,null),damageStat * 0.5f);
        }
    }
}
