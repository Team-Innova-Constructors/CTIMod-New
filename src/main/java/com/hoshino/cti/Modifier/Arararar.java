package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Arararar extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.DURABILITY.add(builder, 400 * modifier.getLevel());
        ToolStats.ARMOR_TOUGHNESS.add(builder, 3 * modifier.getLevel());
        ToolStats.ARMOR.add(builder, 4 * modifier.getLevel());
    }
}
