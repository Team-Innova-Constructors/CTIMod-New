package com.hoshino.cti.Modifier.genre.insatiable;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class ReplacedInsatiable extends BasicInsatiableModifier{
    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 8;
    }
}
