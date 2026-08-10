package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;

public class Emp extends EntityTicker {
    public Emp(){
        super(MobEffectCategory.HARMFUL);
    }
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return false;
    }
}
