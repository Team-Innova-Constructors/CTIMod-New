package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;

import static com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.defense.EnderSuppress.KEY_TARGET_ID;

public class EnderSuppress extends EntityTicker {
    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (entity instanceof ServerPlayer player&&player.getMainHandItem().getItem() instanceof IModifiable
        &&player.getPersistentData().contains(KEY_TARGET_ID)){
            var targetId = player.getPersistentData().getUUID(KEY_TARGET_ID);
            var target = player.getLevel().getEntity(targetId);
            if (target!=null&&target.isAlive())
                ToolAttackUtil.attackEntity(player.getMainHandItem(),player,target);
            player.getPersistentData().remove(KEY_TARGET_ID);
        }
    }
}
