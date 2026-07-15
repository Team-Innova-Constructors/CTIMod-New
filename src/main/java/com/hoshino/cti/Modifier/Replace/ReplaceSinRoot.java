package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.damagesource.DamageSource;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplaceSinRoot extends Modifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var target=context.getLivingTarget();
        var player=context.getPlayerAttacker();
        if(target==null||player==null)return;
        if(!context.isFullyCharged())return;
        var amountMax=100 * modifier.getLevel();
        target.invulnerableTime=0;
        target.hurt(DamageSource.playerAttack(player),Math.min(amountMax,target.getMaxHealth() * 0.03f));
    }
}
