package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.base.BasicBurstModifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class Overclock extends BasicBurstModifier {
    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burst.setMana(burst.getMana()+100*modifier.getLevel());
        burstExtras.addEntityPerConsumption(25*modifier.getLevel());
        burstExtras.addBlockPerConsumption(25*modifier.getLevel());
    }
}
