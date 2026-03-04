package com.hoshino.cti.Modifier.genre.insatiable;

import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplacedBenthamism extends BasicInsatiableModifier{
    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 128;
    }

    @Override
    public int getInsatiableLevel() {
        return 4;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return (float) (damage*Math.pow(0.75,modifier.getLevel()));
    }
}
