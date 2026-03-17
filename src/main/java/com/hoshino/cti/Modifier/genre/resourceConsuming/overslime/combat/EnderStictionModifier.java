package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class EnderStictionModifier extends BasicOverslimeModifier {
    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return 5*modifier.getLevel();
    }

    @Override
    public float getDamageBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*25;
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*500;
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        var attacker = context.getAttacker();
        var level = attacker.level;
        var target = context.getTarget();
        var os = TinkerModifiers.overslime.get();
        if (context.isFullyCharged()&&os.getShield(tool)>=10) {
            if (target.invulnerableTime!=0){
                os.addOverslime(tool,modifier,-5);
                target.invulnerableTime = 0;
            }
            var instance = EntityTickerManager.getInstance(target);
            os.addOverslime(tool,modifier,-5);
            instance.addTicker(new EntityTickerInstance(CtiEntityTickers.ENDER_STICTION.get(), 1,200+100*modifier.getLevel()),Integer::max,Integer::max);
        }
        return knockback;
    }
}
