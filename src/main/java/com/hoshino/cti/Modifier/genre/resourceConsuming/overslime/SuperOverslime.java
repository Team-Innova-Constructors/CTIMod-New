package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

public class SuperOverslime extends EtSTBaseModifier {
    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        OverslimeModifier.OVERSLIME_STAT.multiply(builder,100);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
}
