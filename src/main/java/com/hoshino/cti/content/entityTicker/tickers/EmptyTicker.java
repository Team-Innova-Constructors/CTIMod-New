package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.effect.MobEffectCategory;
import org.jetbrains.annotations.Nullable;

public class EmptyTicker extends EntityTicker {
    public EmptyTicker(@Nullable MobEffectCategory category) {
        super(category);
    }
    public EmptyTicker() {
        super(null);
    }
}
