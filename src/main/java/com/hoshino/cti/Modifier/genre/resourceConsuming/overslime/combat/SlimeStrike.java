package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class SlimeStrike extends BasicOverslimeModifier {

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel();
    }

    @Override
    public float getDamageMul(IToolContext context, ModifierEntry modifier) {
        return 0.05f*modifier.getLevel();
    }

    @Override
    public float getDamageBase(IToolContext context, ModifierEntry modifier) {
        return modifier.getLevel()*10;
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return 50*modifier.getLevel();
    }
}
