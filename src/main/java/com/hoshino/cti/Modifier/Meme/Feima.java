package com.hoshino.cti.Modifier.Meme;

import com.hoshino.cti.Cti;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Feima extends NoLevelsModifier implements MeleeHitModifierHook {
    public static final ResourceLocation FEIMA= Cti.getResource("feima");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var current=tool.getPersistentData().getInt(FEIMA);
        tool.getPersistentData().putInt(FEIMA,current+1);
        if(current%2==0){
            var player=context.getPlayerAttacker();
            var target=context.getLivingTarget();
            if(player==null||target==null)return;
            var a=player.getMaxHealth();
            var b=player.getArmorValue();

            var c=Math.sqrt((a * a) + (b * b));
            context.getLivingTarget().hurt(DamageSource.playerAttack(player).setMagic(),(float) c);
        }
    }
}
