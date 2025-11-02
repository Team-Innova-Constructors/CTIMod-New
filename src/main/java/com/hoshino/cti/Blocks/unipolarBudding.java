package com.hoshino.cti.Blocks;

import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.util.BiomeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;


public class unipolarBudding extends AmethystBlock {
    private static final Direction[] DIRECTIONS = Direction.values();

    public unipolarBudding(Properties p_49795_) {
        super(p_49795_);
    }


    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (pLevel.getDayTime() % 24000 >= 1000 && pLevel.getDayTime() % 24000 <= 13000 && pLevel.getBiome(blockpos).is(BiomeUtil.DISORDERED_ZONE) && ((blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR) || blockstate.is(Blocks.WATER)))) {
            Block block = CtiBlock.unipolar_magnet.get();
            BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
            pLevel.setBlockAndUpdate(blockpos, blockState);
        }
        direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
        blockpos = pPos.relative(direction);
        blockstate = pLevel.getBlockState(blockpos);
        if (pLevel.getDayTime() % 24000 >= 1000 && pLevel.getDayTime() % 24000 <= 13000 && pLevel.getBiome(blockpos).is(BiomeUtil.DISORDERED_ZONE) && ((blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR) || blockstate.is(Blocks.WATER)))) {
            Block block = CtiBlock.unipolar_magnet.get();
            BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
            pLevel.setBlockAndUpdate(blockpos, blockState);
        }
    }
}
