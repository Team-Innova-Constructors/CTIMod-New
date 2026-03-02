package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Fiery extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (duration%10==0){
            int inv = entity.invulnerableTime;
            if (entity instanceof LivingEntity living&&living.getLastHurtByMob()!=null)
                entity.hurt(new EntityDamageSource(DamageSource.ON_FIRE.msgId,living.getLastHurtByMob()),level);
            else entity.hurt(DamageSource.ON_FIRE,level);
            entity.invulnerableTime = inv;
        }
        return true;
    }
}
