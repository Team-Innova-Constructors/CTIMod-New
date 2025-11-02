package com.hoshino.cti.mixin.IAFMixin;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.github.alexthe666.iceandfire.world.gen.TypedFeature;
import com.github.alexthe666.iceandfire.world.gen.WorldGenDragonCave;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = WorldGenDragonCave.class,remap = false)
public abstract class DragonCaveMixin extends Feature<NoneFeatureConfiguration> implements TypedFeature {
    public DragonCaveMixin(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Shadow public boolean isMale;
    @Shadow public abstract void generateCave(LevelAccessor worldIn, int radius, int amount, BlockPos center, RandomSource rand);
    @Shadow
    protected abstract EntityDragonBase createDragon(final WorldGenLevel worldGen, final RandomSource random, final BlockPos position, int dragonAge);

    /**
     * @author EtSH_C2H6S
     * @reason 使龙穴能在深园的较高高度生成。
     */
    @Overwrite
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel worldIn = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        if (rand.nextInt(IafConfig.generateDragonDenChance) != 0 || !IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position) || !IafWorldRegistry.isFarEnoughFromDangerousGen(worldIn, position, getId(), getFeatureType())) {
            return false;
        }
        isMale = rand.nextBoolean();
        ChunkPos chunkPos = worldIn.getChunk(position).getPos();

        //将初始高度改为90
        int j = 90;
        j -= rand.nextInt(30);

        // If the cave generation point is too low
        if (j < worldIn.getMinBuildHeight() + 20) {
            return false;
        }
        // Center the position at the "middle" of the chunk
        position = new BlockPos((chunkPos.x << 4) + 8, j, (chunkPos.z << 4) + 8);
        int dragonAge = 75 + rand.nextInt(50);
        int radius = (int) (dragonAge * 0.2F) + rand.nextInt(4);
        generateCave(worldIn, radius, 3, position, rand);
        EntityDragonBase dragon = createDragon(worldIn, rand, position, dragonAge);
        worldIn.addFreshEntity(dragon);
        return true;
    }
}
