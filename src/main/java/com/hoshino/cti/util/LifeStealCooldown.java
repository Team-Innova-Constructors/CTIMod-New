package com.hoshino.cti.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;


public final class LifeStealCooldown {

    public static final String KEY_NEXT = "cti_lifesteal_next";
    public static final long INTERVAL = 5L * 20L;

    private LifeStealCooldown() {
    }

    public static boolean isReady(Player player) {
        return player.level.getGameTime() >= player.getPersistentData().getLong(KEY_NEXT);
    }

    public static boolean tryTrigger(Player player) {
        CompoundTag data = player.getPersistentData();
        long now = player.level.getGameTime();
        if (now < data.getLong(KEY_NEXT)) {
            return true;
        }
        data.putLong(KEY_NEXT, now + INTERVAL);
        return false;
    }
}
