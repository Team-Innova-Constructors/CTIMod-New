package com.hoshino.cti.Modifier.slot;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class Hardcore extends ArmorModifier {
    @Override
    public boolean hidden() {
        return true;
    }

    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, @NotNull ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(SlotType.DEFENSE, 2);
    }
}
