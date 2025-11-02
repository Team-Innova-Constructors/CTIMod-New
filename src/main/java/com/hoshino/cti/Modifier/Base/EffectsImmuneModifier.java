package com.hoshino.cti.Modifier.Base;

import net.minecraft.world.effect.MobEffect;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.armor.EffectImmunityModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;

import java.util.Collection;
import java.util.List;

public abstract class EffectsImmuneModifier extends Modifier {
    public abstract @NotNull Collection<MobEffect> getImmuneEffects();

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        this.getImmuneEffects().forEach(mobEffect -> builder.addModule(new EffectImmunityModule(mobEffect)));
    }
}
