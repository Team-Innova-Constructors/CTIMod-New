package com.hoshino.cti.Modifier.genre.elemental.fiery;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class Scorched extends BasicBurntModifier{
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel();
    }

    @Override
    public float getBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0.75f*modifierEntry.getLevel();
    }
}
