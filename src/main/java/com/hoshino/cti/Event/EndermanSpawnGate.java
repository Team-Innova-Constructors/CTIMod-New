package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.Cti;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class EndermanSpawnGate {

    public static final ResourceLocation VISITED_NETHER = new ResourceLocation("minecraft", "story/enter_the_nether");

    private static final double NEAREST_PLAYER_DISTANCE = -1.0D;

    private EndermanSpawnGate() {
    }

    @SubscribeEvent
    public static void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getSpawnReason() != MobSpawnType.NATURAL) return;
        Mob mob = event.getEntity();
        if (!(mob instanceof EnderMan)) return;
        var levelAccessor = event.getLevel();
        if (!(levelAccessor instanceof Level level)) {
            event.setResult(Result.DENY);
            return;
        }
        var spawner = level.getNearestPlayer(event.getX(), event.getY(), event.getZ(),
                NEAREST_PLAYER_DISTANCE, false);
        if (spawner == null || !allowsEndermanSpawn(spawner)) {
            event.setResult(Result.DENY);
        }
    }

    public static boolean allowsEndermanSpawn(Player player) {
        return !blocksEndermanSpawn(player);
    }


    public static boolean blocksEndermanSpawn(Player player) {
        return SuperpositionHandler.isTheCursedOne(player) && !hasVisitedNether(player);
    }


    public static boolean hasVisitedNether(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return false;
        MinecraftServer server = serverPlayer.getServer();
        if (server == null) return false;
        Advancement advancement = server.getAdvancements().getAdvancement(VISITED_NETHER);
        return advancement != null && serverPlayer.getAdvancements().getOrStartProgress(advancement).isDone();
    }
}
