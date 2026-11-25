package com.hoshino.cti.content.entityTicker.tickers;

import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class AtomicDecompose extends EntityTicker {
    @Override
    public boolean tickPlayerSpecific(int duration, int level, Entity entity, Player player) {
        if (level<0) return true;
        Random random = new Random();
        var it = entity.invulnerableTime;
        if (level<20){
            if (random.nextInt(20)<level-1){
                entity.invulnerableTime = 0;
                entity.hurt(ElementalDamageSource.chaotic(player,10),20);
                ((ServerLevel)player.level).sendParticles(etshtinkerParticleType.atomic_dec.get(),entity.getX(),entity.getY()+0.5*entity.getBbHeight(),entity.getZ(),1,entity.getBbWidth()/2,entity.getBbHeight()/4,entity.getBbWidth()/2,0);
            }
        } else {
            for (int i = 0;i<level/20;i++){
                entity.invulnerableTime = 0;
                entity.hurt(ElementalDamageSource.chaotic(player,10),20);
                ((ServerLevel)player.level).sendParticles(etshtinkerParticleType.atomic_dec.get(),entity.getX(),entity.getY()+0.5*entity.getBbHeight(),entity.getZ(),1,entity.getBbWidth()/2,entity.getBbHeight()/4,entity.getBbWidth()/2,0);
            }
        }
        if (random.nextFloat()<0.25f&&level>0){
            var tickerInst = EntityTickerManager.getInstancePlayerSpecific(entity,player.getUUID()).getTicker(CtiEntityTickers.ATOMIC_DECOMPOSE.get());
            if (tickerInst!=null) tickerInst.level-=level/5;
        }
        entity.invulnerableTime = it;
        return true;
    }
}
