package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PEjianS2C;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Cti.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Ejian extends EntityTicker {

    public static final String KEY_END = "cti_ejian_end";

    public static final int DURATION = 2400;

    public static final float DAMAGE_MULTIPLIER = 0.05F;

    public static final float HEAL_MULTIPLIER = 0.5F;

    public static final float BREAK_HEALTH_RATIO = 0.95F;

    public static final int DAWN_EXTRA_DAMAGE_DURATION = 15 * 20;

    private static final int SYNC_INTERVAL = 40;

    public Ejian() {
        super(MobEffectCategory.HARMFUL);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHurt);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingHeal);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDeath);
    }

    @Override
    public void onTickerStart(int duration, int level, Entity entity) {
        if (entity instanceof ServerPlayer player) {
            sync(player, duration);
        }
    }

    @Override
    public void onTickerEnd(int level, Entity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        CtiPacketHandler.sendToTrackingAndPlayer(new PEjianS2C(player.getUUID(), false, 0), player);
    }

    @Override
    public boolean tick(int duration, int level, Entity entity) {
        if (!(entity instanceof ServerPlayer player)) {
            return true;
        }
        if (!player.isAlive() || player.isDeadOrDying()) {
            end(player);
            return true;
        }
        if (player.getHealth() >= player.getMaxHealth() * BREAK_HEALTH_RATIO) {
            EntityTickerManager.getInstance(player).addTicker(
                    new EntityTickerInstance(CtiEntityTickers.DAWN_EXTRA_DAMAGE.get(), Math.max(1, level), DAWN_EXTRA_DAMAGE_DURATION),
                    Integer::max, Integer::max);
            end(player);
        }
        return true;
    }


    private static void end(ServerPlayer player) {
        EntityTickerInstance instance = EntityTickerManager.getInstance(player).getTicker(CtiEntityTickers.EJIAN.get());
        if (instance != null) {
            instance.duration = 0;
        }
    }

    private static void sync(ServerPlayer player, int duration) {
        CtiPacketHandler.sendToTrackingAndPlayer(new PEjianS2C(player.getUUID(), true, duration), player);
    }

    public static boolean hasEjian(Player player) {
        return EntityTickerManager.getInstance(player).hasTicker(CtiEntityTickers.EJIAN.get());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level.isClientSide) {
            return;
        }
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }
        CompoundTag data = player.getPersistentData();
        boolean restored = false;
        if (data.contains("cti_tickers")) {
            EntityTickerManager.load(player);
            restored = true;
        }
        if (!hasEjian(player)) {
            if (data.contains(KEY_END)) {
                data.remove(KEY_END);
            }
            return;
        }
        long now = player.level.getGameTime();
        long end = data.getLong(KEY_END);
        if (end <= 0L) {
            data.putLong(KEY_END, now + DURATION);
            return;
        }
        if (restored || player.tickCount % SYNC_INTERVAL == 0) {
            sync(player, (int) Math.max(1L, end - now));
        }
        if (now >= end) {
            EntityTickerManager.getInstance(player).removeTicker(CtiEntityTickers.EJIAN.get());
        }
    }

    private void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player && hasEjian(player)) {
            event.setAmount(event.getAmount() * DAMAGE_MULTIPLIER);
        }
    }

    private void onLivingHeal(LivingHealEvent event) {
        if (event.getEntity() instanceof Player player && hasEjian(player)) {
            event.setAmount(event.getAmount() * HEAL_MULTIPLIER);
        }
    }

    private void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && hasEjian(player)) {
            EntityTickerManager.getInstance(player).removeTicker(this);
        }
    }
}
