package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class InvulnerableTicker extends EntityTicker {
    @Override
    public void onTickerStart(int duration, int level, Entity entity) {
        entity.setGlowingTag(true);
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (entity.level instanceof ServerLevel serverLevel) serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,entity.getX(),entity.getY(),entity.getZ(),5,entity.getBbWidth()/2,entity.getBbHeight(),entity.getBbWidth()/2,0.1);
        return true;
    }

    @Override
    public void onTickerEnd(int level, Entity entity) {
        entity.setGlowingTag(false);
    }
}
