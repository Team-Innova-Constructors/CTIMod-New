package com.hoshino.cti.mixin.IFMixin;

import com.buuz135.industrial.block.misc.StasisChamberBlock;
import com.buuz135.industrial.block.misc.tile.StasisChamberTile;
import com.buuz135.industrial.block.tile.IndustrialAreaWorkingTile;
import com.buuz135.industrial.block.tile.IndustrialWorkingTile;
import com.buuz135.industrial.block.tile.RangeManager;
import com.buuz135.industrial.config.machine.misc.StasisChamberConfig;
import com.buuz135.industrial.utils.IndustrialTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(value = StasisChamberTile.class,remap = false)
public abstract class StasisChamberTileMixin extends IndustrialAreaWorkingTile<StasisChamberTile> {
    @Shadow
    private int getPowerPerOperation;

    public StasisChamberTileMixin(Pair<RegistryObject<Block>, RegistryObject<BlockEntityType<?>>> basicTileBlock, RangeManager.RangeType type, boolean acceptsRangeUpgrades, int estimatedPower, BlockPos blockPos, BlockState blockState) {
        super(basicTileBlock, type, acceptsRangeUpgrades, estimatedPower, blockPos, blockState);
    }

    /**
     * @author
     * @reason 阻止冷凝机瞎冷凝生物，加了大师球同款黑名单
     */
    @Overwrite
    public IndustrialWorkingTile.WorkAction work() {
        if (hasEnergy(this.getPowerPerOperation)) {
            List<Mob> entities = this.level.getEntitiesOfClass(Mob.class, getWorkingArea().bounds());
            for (Mob entity : entities) {
                if (entity.getType().is(IndustrialTags.EntityTypes.MOB_IMPRISONMENT_TOOL_BLACKLIST)) continue;
                entity.setNoAi(true);
                entity.getPersistentData().putLong("StasisChamberTime", this.level.getGameTime());
                if (!entity.canChangeDimensions() && level instanceof ServerLevel) {
                    if (StasisChamberConfig.disableBossBars) {
                        level.players().forEach(entity1 -> entity.stopSeenByPlayer((ServerPlayer) entity1));
                    } else {
                        level.players().forEach(entity1 -> entity.startSeenByPlayer((ServerPlayer) entity1));
                    }
                }
                if (level.random.nextBoolean() && level.random.nextBoolean()) entity.heal(1f);
            }
            List<Player> players = this.level.getEntitiesOfClass(Player.class, getWorkingArea().bounds());
            players.forEach(playerEntity -> {
                playerEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 25, 135));
                if (level.random.nextBoolean()) playerEntity.heal(1f);
            });
            return new IndustrialWorkingTile.WorkAction(0.5f, getPowerPerOperation);
        }
        return new IndustrialWorkingTile.WorkAction(1, 0);
    }
}
