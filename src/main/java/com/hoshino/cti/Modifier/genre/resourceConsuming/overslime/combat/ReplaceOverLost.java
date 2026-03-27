package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.content.materialGenre.GenreManager;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class ReplaceOverLost extends BasicOverslimeModifier {
    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*100;
    }

    @Override
    public float getDamageBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*15;
    }

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return 5;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (context.getTarget() instanceof LivingEntity living){
            var slowEffect = living.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
            if (slowEffect!=null){
                var duration = slowEffect.getDuration();
                var amp = slowEffect.getAmplifier()+1;
                var boostAdd = duration/10;
                boostAdd =Math.min(boostAdd,10);
                var os = TinkerModifiers.overslime.get();
                if (os.getShield(tool)>= boostAdd){
                    os.addOverslime(tool,modifier,-boostAdd);
                    damage+= boostAdd *10;
                }
                var boostMul = amp*0.025f;
                var consumption = boostAdd*(1+boostMul);
                consumption += tool.getStats().getInt(GenreManager.OVERSLIME_GENRE.consumption)*boostMul;
                if (os.getShield(tool)>=consumption){
                    os.addOverslime(tool,modifier, (int) -consumption);
                    damage+=damage*boostMul;
                }
            }
        }
        return damage;
    }
}
