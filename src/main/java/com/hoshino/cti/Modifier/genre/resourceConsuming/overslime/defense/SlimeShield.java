package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.defense;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class SlimeShield extends BasicOverslimeModifier {
    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*2;
    }

    @Override
    public float getArmorBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*5;
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return 50*modifier.getLevel();
    }
}
