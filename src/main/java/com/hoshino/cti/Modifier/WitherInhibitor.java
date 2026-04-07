package com.hoshino.cti.Modifier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class WitherInhibitor extends Modifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var player=context.getPlayerAttacker();
        var target=context.getLivingTarget();
        if(player!=null){
            if(target instanceof WitherBoss witherBoss){
                witherBoss.setNoAi(true);
                var lightningBolt=new LightningBolt(EntityType.LIGHTNING_BOLT,witherBoss.level);
                lightningBolt.setPos(witherBoss.position());
                witherBoss.level.addFreshEntity(lightningBolt);
            }
        }
    }
}
