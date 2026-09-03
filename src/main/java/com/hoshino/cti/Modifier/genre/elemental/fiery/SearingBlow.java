package com.hoshino.cti.Modifier.genre.elemental.fiery;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class SearingBlow extends BasicBurntModifier{
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        int lvl = modifierEntry.getLevel();
        return (lvl*(7+lvl)/2)+12;
    }
}
