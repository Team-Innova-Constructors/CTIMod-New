package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PSyncAttackTickerS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DoubleEdgedRecovery extends EntityTicker {
    public DoubleEdgedRecovery(){
        super();
    }

    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer){
            serverPlayer.attackStrengthTicker = level;
            CtiPacketHandler.sendToPlayer(new PSyncAttackTickerS2C(level),serverPlayer);
        }
    }
}
