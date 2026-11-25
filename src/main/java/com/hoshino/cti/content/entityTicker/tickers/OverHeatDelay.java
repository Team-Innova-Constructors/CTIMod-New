package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class OverHeatDelay extends EntityTicker {
    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (entity instanceof Player player){
            player.addEffect(new MobEffectInstance(CtiEffects.OVERHEAT.get(),200,0,false,false));
        }
    }
}
