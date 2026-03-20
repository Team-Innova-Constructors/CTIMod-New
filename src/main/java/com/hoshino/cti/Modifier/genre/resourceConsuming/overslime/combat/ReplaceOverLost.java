package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
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
                var boost = slowEffect.getAmplifier()+1;
                boost=Math.min(boost,10);
                var os = TinkerModifiers.overslime.get();
                if (os.getShield(tool)>=boost){
                    os.addOverslime(tool,modifier,-boost);
                    damage+=boost*5;
                }
            }
        }
        return damage;
    }
}
