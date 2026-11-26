package com.hoshino.cti.L2;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.entityTicker.tickers.Ejian;
import com.hoshino.cti.register.CtiEntityTickers;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;


public class EjianTrait extends LegendaryTrait {

    public static final String KEY_ARMED = "cti_ejian_armed";
    public static final String KEY_COOLDOWN = "cti_ejian_cooldown";

    public static final int ATTEMPT_INTERVAL = 20;
    public static final int MOB_COOLDOWN = 1200;
    public static final int DURATION = Ejian.DURATION;
    public static final float HEALTH_COST = 0.3F;

    public EjianTrait() {
        super(ChatFormatting.RED);
    }

    @Override
    public void tick(LivingEntity entity, int level) {
        if (entity.level.isClientSide || !(entity instanceof Mob mob) || !mob.isAlive()) {
            return;
        }
        CompoundTag data = mob.getPersistentData();
        boolean armed = !data.contains(KEY_ARMED) || data.getBoolean(KEY_ARMED);
        long gameTime = mob.level.getGameTime();
        boolean aboveHalf = mob.getHealth() > mob.getMaxHealth() * 0.5F;
        boolean cooldownOver = data.getLong(KEY_COOLDOWN) <= gameTime;
        if ((aboveHalf || cooldownOver) && !armed) {
            data.putBoolean(KEY_ARMED, true);
            armed = true;
        }
        if (aboveHalf) {
            return;
        }
        if (!armed) {
            return;
        }
        if (mob.tickCount % ATTEMPT_INTERVAL != 0) {
            return;
        }
        if (!cooldownOver) {
            return;
        }
        if (!(mob.getTarget() instanceof ServerPlayer target)) {
            return;
        }
        if (!target.isAlive() || target.isDeadOrDying()) {
            return;
        }
        if (Ejian.hasEjian(target)) {
            return;
        }
        apply(target, level);
        data.putBoolean(KEY_ARMED, false);
        data.putLong(KEY_COOLDOWN, mob.level.getGameTime() + MOB_COOLDOWN);
    }

    public static void apply(ServerPlayer player, int level) {
        float remaining = player.getHealth() - player.getMaxHealth() * HEALTH_COST;
        player.setHealth(Math.max(1.0F, remaining));
        player.getPersistentData().putLong(Ejian.KEY_END, player.level.getGameTime() + DURATION);
        EntityTickerManager.getInstance(player).addTickerSimple(new EntityTickerInstance(CtiEntityTickers.EJIAN.get(), Math.max(1, level), DURATION));
    }
}
