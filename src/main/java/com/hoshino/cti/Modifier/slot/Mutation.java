package com.hoshino.cti.Modifier.slot;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class Mutation extends BattleModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public boolean hidden() {
        return true;
    }

    @Override
    public void addVolatileData(IToolContext iToolContext, @NotNull ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(SlotType.ABILITY, 1);
        modDataNBT.addSlots(SlotType.UPGRADE, 1);
        modDataNBT.addSlots(SlotType.DEFENSE, 1);
    }
}
