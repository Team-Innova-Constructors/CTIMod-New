package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.base.BasicBurstModifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class PulseEngine extends BasicBurstModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void updateBurst(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtra) {
        var entity = burst.entity();
        entity.setDeltaMovement(entity.getDeltaMovement().scale(1.1f));
    }
}
