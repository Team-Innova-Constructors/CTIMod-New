package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplacedConducting extends EtSTBaseModifier {

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var target = context.getLivingTarget();
        var player = context.getPlayerAttacker();
        if (target==null||player==null) return damage;
        float fireTicks = target.getRemainingFireTicks();
        var instance = EntityTickerManager.getInstance(target);
        if (instance.hasTicker(CtiEntityTickers.FIERY.get()))
            fireTicks = Math.max(fireTicks,instance.getTicker(CtiEntityTickers.FIERY.get()).duration);
        fireTicks/=20f;
        float bonus = Math.min(fireTicks*0.025f*modifier.getLevel(),0.2f+0.25f*modifier.getLevel());
        return damage+baseDamage*bonus;
    }
}
