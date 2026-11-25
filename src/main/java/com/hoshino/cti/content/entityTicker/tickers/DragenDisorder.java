package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class DragenDisorder extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (entity.level instanceof ServerLevel serverLevel&&serverLevel.getGameTime()%5==0){
            serverLevel.sendParticles(CtiParticleType.DRAGON_DISORDER.get(),entity.getX(),entity.getY()+0.5*entity.getBbHeight(),entity.getZ(),2,entity.getBbWidth()/2,entity.getBbHeight()/4,entity.getBbWidth()/2,0.15f);
        }
        return true;
    }
}
