package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.entity.Entity;

public class Emp extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return false;
    }
}
