package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait.OverslimeHandler;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.List;
import static com.c2h6s.etshtinker.init.etshtinkerToolStats.*;

public class OverTrade extends BasicOverslimeModifier {
    @Override
    public int getPriority() {
        return OverslimeHandler.OVERSLIME_MODIFIER_PRIORITY-2;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getDamageBase(IToolContext context, ModifierEntry modifier) {
        return -50;
    }

    @Override
    public float getDamageMul(IToolContext context, ModifierEntry modifier) {
        return -0.5f;
    }

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return 50;
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        OverslimeModifier.OVERSLIME_STAT.percent(builder,0.5f);
        List.of(ToolStats.ATTACK_DAMAGE,ToolStats.DURABILITY,ToolStats.MINING_SPEED,ToolStats.ATTACK_SPEED, DAMAGEMULTIPLIER,SCALE).forEach(
                stat-> stat.multiply(builder,2)
        );
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var overslime = TinkerModifiers.overslime.get();
        if (overslime.getShield(tool)<=0) return 0;
        return damage;
    }
}
