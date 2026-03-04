package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class StoneColdReplaced extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return InsatiableHandler.PRIORITY+2;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getTarget() instanceof LivingEntity living&&living.getHealth()>=living.getMaxHealth()*0.33f){
            var it = living.invulnerableTime;
            var lastHurt = living.lastHurt;
            for (int i = 0; i < modifier.getLevel()*2; i++) {
                living.invulnerableTime=0;
                living.hurt(DamageSource.mobAttack(context.getAttacker()),damage*0.05f);
            }
            living.invulnerableTime=it;
            living.lastHurt = lastHurt;
        }
    }
}
