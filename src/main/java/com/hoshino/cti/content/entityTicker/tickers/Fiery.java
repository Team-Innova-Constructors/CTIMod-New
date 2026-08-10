package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class Fiery extends EntityTicker {
    public Fiery() {
        super(MobEffectCategory.HARMFUL);
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        return true;
    }

    @Override
    public boolean tickPlayerSpecific(int duration, int level, Entity entity, Player player) {
        if (duration%10==0){
            int inv = entity.invulnerableTime;
            entity.hurt(new EntityDamageSource(DamageSource.ON_FIRE.msgId,player),level);
            entity.invulnerableTime = inv;
        }
        return true;
    }
}
