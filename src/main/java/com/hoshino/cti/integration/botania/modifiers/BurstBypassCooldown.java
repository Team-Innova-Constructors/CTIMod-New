package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.base.BasicBurstModifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class BurstBypassCooldown extends BasicBurstModifier {
    @Override
    public void beforeBurstHitEntity(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @NotNull Entity owner, @NotNull Entity target, ManaBurst burst, IManaBurstExtra burstExtra, float damage) {
        target.invulnerableTime = 0;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
}
