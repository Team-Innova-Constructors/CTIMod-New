package com.hoshino.cti.Modifier.Base;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public abstract class OxygenConsumeModifier extends ArmorModifier {
    public abstract boolean hasOxygen(IToolStackView tool, ModifierEntry modifier);
    public abstract void consumeOxygen(IToolStackView tool, ModifierEntry modifier);
}
