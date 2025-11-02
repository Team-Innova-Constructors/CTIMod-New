package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SuperCritHit extends BattleModifier{
    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if(context.isCritical()){
            float per=attacker.getHealth()/attacker.getMaxHealth();
            return damage * (1+per * 2);
        }
        return damage;
    }
}
