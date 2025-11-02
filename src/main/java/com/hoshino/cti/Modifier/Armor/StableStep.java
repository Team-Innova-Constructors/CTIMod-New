package com.hoshino.cti.Modifier.Armor;

import com.hoshino.cti.Modifier.Base.EffectsImmuneModifier;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class StableStep extends EffectsImmuneModifier {
    @Override
    public @NotNull Collection<MobEffect> getImmuneEffects() {
        return List.of(
                MobEffects.SLOW_FALLING,
                MobEffects.LEVITATION,
                LHEffects.MOONWALK.get()
        );
    }
}
