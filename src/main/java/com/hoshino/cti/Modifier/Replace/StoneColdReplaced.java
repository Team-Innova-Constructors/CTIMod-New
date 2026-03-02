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
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getTarget() instanceof LivingEntity living&&living.getHealth()>=living.getMaxHealth()*0.5f){
            var it = living.invulnerableTime;
            for (int i = 0; i < modifier.getLevel()*2; i++) {
                living.invulnerableTime=0;
                living.hurt(DamageSource.mobAttack(context.getAttacker()),damageDealt*0.05f);
            }
            living.invulnerableTime=it;
        }
    }
}
