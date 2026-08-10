package com.hoshino.cti.content.entityTicker;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

//EntityTicker的总控，可以很方便的来添加/减少Ticker。
public class EntityTickerManager {
    public static final ConcurrentHashMap<UUID, ConcurrentHashMap<EntityTicker, EntityTickerInstance>> TICKER_MAP = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID,ConcurrentHashMap<UUID,ConcurrentHashMap<EntityTicker,EntityTickerInstance>>> PLAYER_SPECIFIC_TICKER_MAP = new ConcurrentHashMap<>();

    public static EntityTickerManagerInstance getInstance(Entity entity) {
        return new EntityTickerManagerInstance(entity);
    }
    public static EntityTickerManagerInstance getInstancePlayerSpecific(Entity entity,UUID playerUUID) {
        return new EntityTickerManagerInstance(entity,playerUUID);
    }

    public static boolean tick(Entity entity) {
        UUID uuid = entity.getUUID();
        if (entity.isRemoved()) {
            TICKER_MAP.remove(uuid);
            return true;
        }
        if (entity.getPersistentData().contains("cti_tickers")||entity.getPersistentData().contains("cti_player_tickers")) {
            load(entity);
        }
        return tickCommon(entity,uuid)&&tickPlayerSpecific(entity,uuid);
    }

    public static boolean tickCommon(Entity entity,UUID uuid){
        if (!TICKER_MAP.containsKey(uuid)) return true;
        ConcurrentHashMap<EntityTicker, EntityTickerInstance> entityTickers = TICKER_MAP.get(uuid);
        if (entityTickers == null || entityTickers.isEmpty()) {
            TICKER_MAP.remove(uuid);
            return true;
        }
        boolean doTick = true;
        EntityTickerManagerInstance managerInstance = getInstance(entity);
        List<EntityTickerInstance> instancesCopy = List.copyOf(entityTickers.values());
        for (EntityTickerInstance instance : instancesCopy) {
            EntityTicker ticker = instance.ticker;
            if (!ticker.isInfinite()) instance.duration--;
            doTick = doTick && ticker.tick(instance.duration, instance.level, entity);
            if (instance.duration > 0) {
                managerInstance.setTicker(instance);
            } else {
                managerInstance.removeTicker(ticker);
            }
        }
        if (entity instanceof Player) return true;
        return doTick;
    }
    public static boolean tickPlayerSpecific(Entity entity,UUID uuid){
        if (!PLAYER_SPECIFIC_TICKER_MAP.containsKey(uuid)) return true;
        var map = PLAYER_SPECIFIC_TICKER_MAP.get(uuid);
        if (map == null||map.isEmpty()){
            PLAYER_SPECIFIC_TICKER_MAP.remove(uuid);
            return true;
        }
        var uuids = List.copyOf(map.keySet());
        boolean doTick = true;
        for (UUID playerID:uuids){
            var entityTickers = map.get(playerID);
            if (entityTickers == null || entityTickers.isEmpty()) {
                map.remove(playerID);
                continue;
            }
            EntityTickerManagerInstance managerInstance = getInstancePlayerSpecific(entity,playerID);
            List<EntityTickerInstance> instancesCopy = List.copyOf(entityTickers.values());
            for (EntityTickerInstance instance : instancesCopy) {
                EntityTicker ticker = instance.ticker;
                if (!ticker.isInfinite()) instance.duration--;
                if (entity.getServer()!=null) {
                    var player = entity.getServer().getPlayerList().getPlayer(playerID);
                    if (player!=null) doTick = doTick && ticker.tickPlayerSpecific(instance.duration, instance.level, entity,player);
                }
                if (instance.duration > 0) {
                    managerInstance.setTicker(instance);
                } else {
                    managerInstance.removeTicker(ticker);
                }
            }

        }
        if (entity instanceof Player) return true;
        return doTick;
    }

    public static void saveAll(MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (TICKER_MAP.containsKey(entity.getUUID())) {
                    EntityTickerManager.save(entity);
                }
            }
        }
        TICKER_MAP.clear();
    }

    public static void load(Entity entity) {
        if (entity.getPersistentData().contains("cti_tickers"))
            loadCommon(entity);
    }

    public static void loadCommon(Entity entity){
        CompoundTag nbt = entity.getPersistentData().getCompound("cti_tickers");
        ConcurrentHashMap<EntityTicker, EntityTickerInstance> instances = new ConcurrentHashMap<>();
        if (!nbt.isEmpty()) {
            nbt.getAllKeys().forEach(string -> {
                if (nbt.contains(string, CompoundTag.TAG_COMPOUND)) {
                    EntityTickerInstance instance = EntityTickerInstance.readFromNbt(nbt.getCompound(string), new ResourceLocation(string));
                    if (instance != null) instances.put(instance.ticker, instance);
                }
            });
        }
        TICKER_MAP.put(entity.getUUID(), instances);
        entity.getPersistentData().remove("cti_tickers");
    }

    public static void save(Entity entity) {
        saveCommon(entity);
    }

    public static void saveCommon(Entity entity){
        if (entity == null) return;
        CompoundTag nbt = new CompoundTag();
        ConcurrentHashMap<EntityTicker, EntityTickerInstance> instances = TICKER_MAP.get(entity.getUUID());
        if (instances != null) {
            instances.values().forEach(instance -> instance.writeToNbt(nbt));
            entity.getPersistentData().put("cti_tickers", nbt);
        }
    }

    public static class EntityTickerManagerInstance {

        protected @Nullable ConcurrentHashMap<EntityTicker, EntityTickerInstance> instanceMap;
        public final Entity entity;
        public final UUID uuid;
        public final @Nullable UUID playerUUID;

        protected EntityTickerManagerInstance(Entity entity) {
            this.entity = entity;
            this.uuid = entity.getUUID();
            this.instanceMap = TICKER_MAP.get(this.uuid);
            this.playerUUID = null;
        }
        protected EntityTickerManagerInstance(Entity entity,@NotNull UUID playerUUID) {
            this.entity = entity;
            this.uuid = entity.getUUID();
            if (!PLAYER_SPECIFIC_TICKER_MAP.containsKey(this.uuid))
                PLAYER_SPECIFIC_TICKER_MAP.put(this.uuid,new ConcurrentHashMap<>());
            this.instanceMap = PLAYER_SPECIFIC_TICKER_MAP.get(this.uuid).get(playerUUID);
            this.playerUUID = playerUUID;
        }

        public boolean isPlayerSpecific(){
            return this.playerUUID!=null;
        }

        public boolean hasTicker(EntityTicker ticker) {
            return this.instanceMap != null && instanceMap.containsKey(ticker);
        }

        public @NotNull Optional<EntityTickerInstance> getOptional(EntityTicker type) {
            return Optional.ofNullable(this.getTicker(type));
        }

        public @Nullable EntityTickerInstance getTicker(EntityTicker type) {
            return (this.instanceMap == null) ? null : this.instanceMap.get(type);
        }

        public void setTicker(EntityTickerInstance instance) {
            if (!this.hasTicker(instance.ticker)) {
                instance.ticker.onTickerStart(instance.duration, instance.level, this.entity);
            }
            if (this.instanceMap == null) {
                this.instanceMap = new ConcurrentHashMap<>();
                this.instanceMap.put(instance.ticker, instance);
                if (this.isPlayerSpecific())
                    PLAYER_SPECIFIC_TICKER_MAP.get(this.uuid).put(this.playerUUID,this.instanceMap);
                else TICKER_MAP.put(this.uuid, this.instanceMap);
            } else {
                this.instanceMap.put(instance.ticker, instance);
            }
        }

        public void addTicker(EntityTickerInstance instance, BiFunction<Integer, Integer, Integer> levelFunction, BiFunction<Integer, Integer, Integer> timeFunction) {
            if (this.instanceMap == null) {
                this.instanceMap = new ConcurrentHashMap<>();
                if (this.isPlayerSpecific())
                    PLAYER_SPECIFIC_TICKER_MAP.get(this.uuid).put(this.playerUUID,this.instanceMap);
                else TICKER_MAP.put(this.uuid, this.instanceMap);
            }
            EntityTickerInstance existing = this.instanceMap.get(instance.ticker);
            int existingLevel = existing != null ? existing.level : 0;
            int existingTime = existing != null ? existing.duration : 0;

            EntityTickerInstance merged = new EntityTickerInstance(instance.ticker, levelFunction.apply(existingLevel, instance.level), timeFunction.apply(existingTime, instance.duration));
            this.setTicker(merged);
        }

        public void addTickerSimple(EntityTickerInstance instance) {
            addTicker(instance, Integer::max, Integer::max);
        }

        public void removeTicker(EntityTicker ticker) {
            if (this.instanceMap != null && this.hasTicker(ticker)) {
                ticker.onTickerEnd(this.instanceMap.get(ticker).level, this.entity);
                this.instanceMap.remove(ticker);
            }
        }
    }
}
