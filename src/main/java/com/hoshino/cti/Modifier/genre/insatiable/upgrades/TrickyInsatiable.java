package com.hoshino.cti.Modifier.genre.insatiable.upgrades;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.effect.MobEffectInstance;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.stats.ToolType;

public class TrickyInsatiable extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return InsatiableHandler.PRIORITY+1;
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (context.isCritical()){
            InsatiableHandler.applyEffect(context.getAttacker(), ToolType.MELEE,modifier.getLevel()*2);
            context.getAttacker().addEffect(new MobEffectInstance(CtiEffects.INSATIABLE_BOOST.get(),300,modifier.getLevel()-1,false,false));
        }
        return knockback;
    }
}
