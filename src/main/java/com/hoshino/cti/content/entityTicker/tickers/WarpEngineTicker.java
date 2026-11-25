package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PSyncPlayerMovementS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class WarpEngineTicker extends EntityTicker {
    @Override
    public boolean isInfinite() {
        return true;
    }

    @Override
    public void onTickerStart(int duration, int level, Entity entity) {
        if (entity instanceof Player player){
            var data = player.getPersistentData();
            data.putBoolean("isFlyBeforeWarp",player.getAbilities().flying);
            data.putBoolean("isMayFlyBeforeWarp",player.getAbilities().mayfly);
            player.getAbilities().flying = true;
            player.getAbilities().mayfly = true;
            player.setNoGravity(true);
            player.level.playSound(null,player,SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS,1,1);
        }
    }

    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (entity instanceof ServerPlayer player) {
            var data = player.getPersistentData();
            player.getAbilities().mayfly = data.getBoolean("isMayFlyBeforeWarp");
            player.getAbilities().flying = data.getBoolean("isFlyBeforeWarp");
            player.setNoGravity(false);
            player.noPhysics = false;
            data.remove("isMayFlyBeforeWarp");
            data.remove("isFlyBeforeWarp");
            player.setDeltaMovement(player.getLookAngle().scale(level*2));
            CtiPacketHandler.sendToPlayer(new PSyncPlayerMovementS2C(player.getDeltaMovement(),player.position()),player);
            player.level.playSound(null,player,SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS,1,1);
        }
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (entity instanceof ServerPlayer player){
            player.noPhysics = true;
            player.setDeltaMovement(player.getLookAngle().scale(level*0.5f));
            player.setPos(player.position().add(player.getLookAngle().scale(level*0.5f)));
            CtiPacketHandler.sendToPlayer(new PSyncPlayerMovementS2C(player.getDeltaMovement(),player.position()),player);
        }
        return true;
    }
}
