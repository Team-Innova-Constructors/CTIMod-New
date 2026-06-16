package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Dawn extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if(entity.tickCount%5==0){
            if(entity instanceof LivingEntity living){
                living.heal(living.getMaxHealth() * 0.066f);
            }
        }
        return true;
    }
}
