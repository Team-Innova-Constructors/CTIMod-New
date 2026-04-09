package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.BurstDamageModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.base.BasicBurstModifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class BurstMassEffect extends BasicBurstModifier implements BurstDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, CtiBotModifierHooks.BURST_DAMAGE);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getBurstDamage(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, @NotNull Entity target, ManaBurst burst, IManaBurstExtra burstExtra, float baseDamage, float damage) {
        return damage+baseDamage*(float) burst.entity().getDeltaMovement().length();
    }
}
