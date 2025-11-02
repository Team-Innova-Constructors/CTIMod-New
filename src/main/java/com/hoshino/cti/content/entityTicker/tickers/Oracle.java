package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class Oracle extends EntityTicker {
    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (entity instanceof LivingEntity living){
            for (MobEffect effect: List.copyOf( living.activeEffects.keySet())){
                if (effect.getCategory()== MobEffectCategory.BENEFICIAL){
                    living.onEffectRemoved(living.activeEffects.get(effect));
                    living.activeEffects.remove(effect);
                }
            }
        }
        return true;
    }
}
