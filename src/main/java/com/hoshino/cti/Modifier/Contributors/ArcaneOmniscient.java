package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public class ArcaneOmniscient extends BattleModifier {
    @Override
    public void addVolatileData(IToolContext iToolContext, @NotNull ModifierEntry modifierEntry, ModDataNBT modDataNBT) {
        modDataNBT.addSlots(SlotType.ABILITY, 2 * modifierEntry.getLevel());
        modDataNBT.addSlots(SlotType.DEFENSE, 2 * modifierEntry.getLevel());
        modDataNBT.addSlots(SlotType.UPGRADE, 2 * modifierEntry.getLevel());
    }
}
