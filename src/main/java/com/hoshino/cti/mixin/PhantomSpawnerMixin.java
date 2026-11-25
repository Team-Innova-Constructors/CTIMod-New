package com.hoshino.cti.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @Unique
    private int cti$nextTick;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void cti$vanillaTickOnly(ServerLevel level, boolean spawnEnemies, boolean spawnFriendlies, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(this.cti$vanillaTick(level, spawnEnemies));
    }

    @Unique
    private int cti$vanillaTick(ServerLevel level, boolean spawnEnemies) {
        if (!spawnEnemies) {
            return 0;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
            return 0;
        }
        RandomSource random = level.random;
        --this.cti$nextTick;
        if (this.cti$nextTick > 0) {
            return 0;
        }
        this.cti$nextTick += (60 + random.nextInt(60)) * 20;
        if (level.getSkyDarken() < 5 && level.dimensionType().hasSkyLight()) {
            return 0;
        }
        int spawned = 0;
        for (ServerPlayer player : level.players()) {
            if (player.isSpectator()) {
                continue;
            }
            BlockPos playerPos = player.blockPosition();
            if (level.dimensionType().hasSkyLight() && (playerPos.getY() < level.getSeaLevel() || !level.canSeeSky(playerPos))) {
                continue;
            }
            DifficultyInstance difficulty = level.getCurrentDifficultyAt(playerPos);
            if (!difficulty.isHarderThan(random.nextFloat() * 3.0F)) {
                continue;
            }
            ServerStatsCounter stats = player.getStats();
            int ticksSinceRest = Mth.clamp(stats.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
            if (random.nextInt(ticksSinceRest) < 72000) {
                continue;
            }
            BlockPos spawnPos = playerPos.above(20 + random.nextInt(15))
                    .east(-10 + random.nextInt(21))
                    .south(-10 + random.nextInt(21));
            BlockState state = level.getBlockState(spawnPos);
            FluidState fluid = level.getFluidState(spawnPos);
            if (!NaturalSpawner.isValidEmptySpawnBlock(level, spawnPos, state, fluid, EntityType.PHANTOM)) {
                continue;
            }
            SpawnGroupData groupData = null;
            int count = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);
            for (int i = 0; i < count; ++i) {
                Phantom phantom = EntityType.PHANTOM.create(level);
                if (phantom == null) {
                    continue;
                }
                phantom.moveTo(spawnPos, 0.0F, 0.0F);
                if (ForgeHooks.canEntitySpawn(phantom, level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), (BaseSpawner) null, MobSpawnType.NATURAL) == -1) {
                    --spawned;
                } else {
                    if (!ForgeEventFactory.doSpecialSpawn(phantom, level, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), (BaseSpawner) null, MobSpawnType.NATURAL)) {
                        groupData = phantom.finalizeSpawn(level, difficulty, MobSpawnType.NATURAL, groupData, (CompoundTag) null);
                    }
                    level.addFreshEntityWithPassengers(phantom);
                }
            }
            spawned += count;
        }
        return spawned;
    }
}
