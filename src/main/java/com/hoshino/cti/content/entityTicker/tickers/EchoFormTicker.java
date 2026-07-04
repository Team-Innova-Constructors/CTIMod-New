package com.hoshino.cti.content.entityTicker.tickers;

import com.c2h6s.etshtinker.tools.item.tinker.ConstrainedPlasmaSaber;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;

public class EchoFormTicker extends EntityTicker {
    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer) {
            var strength = serverPlayer.attackStrengthTicker;
            serverPlayer.attackStrengthTicker = level;
            LeftClickModifierHook.handleLeftClick(serverPlayer.getMainHandItem(),serverPlayer, EquipmentSlot.MAINHAND);
            if (serverPlayer.getMainHandItem().getItem() instanceof ConstrainedPlasmaSaber)
                ConstrainedPlasmaSaber.createSlash(serverPlayer);
            serverPlayer.attackStrengthTicker = strength;
        }
    }
}
