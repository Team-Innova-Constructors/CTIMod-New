package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.util.DelayDamageTickerRecord;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectTicker extends EntityTicker {
    public static ConcurrentHashMap<UUID, DelayDamageTickerRecord> DAMAGE_MAP=new ConcurrentHashMap<>();
    @Override
    public void onTickerEnd(int level, Entity entity) {
        UUID targetUuid = entity.getUUID();
        if(!(entity instanceof LivingEntity living))return;
        DelayDamageTickerRecord record = DAMAGE_MAP.remove(targetUuid);
        if (record != null) {
            if(living.getLevel() instanceof ServerLevel serverLevel){
                Entity attacker = serverLevel.getEntity(record.attacker());
                if (attacker instanceof LivingEntity livingAttacker && livingAttacker.isAlive()) {
                    float Magnification = level * 0.3F;
                    float reflectAmount =Math.min(living.getHealth() * Magnification, record.totalDamage()) *0.08f*level;
                    EntityDamageSource mobAttackReflect=new EntityDamageSource("mobattackreflect",living).setThorns();
                    mobAttackReflect.setScalesWithDifficulty().setMagic().bypassArmor();
                    livingAttacker.hurt(mobAttackReflect,reflectAmount);
                }
            }
        }
    }
}
