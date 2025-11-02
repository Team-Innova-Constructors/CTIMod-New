package com.hoshino.cti.Blocks;

import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.AmethystBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

public class RasteriteBudding extends AmethystBlock {
    public RasteriteBudding(Properties p_151999_) {
        super(p_151999_);
    }

    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        if (pRandom.nextInt(144) <= pLevel.getBrightness(LightLayer.BLOCK, blockpos) * pLevel.getBrightness(LightLayer.BLOCK, blockpos)) {
            if ((blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR) || blockstate.is(Blocks.WATER))) {
                Block block = CtiBlock.rasterite.get();
                BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                pLevel.setBlockAndUpdate(blockpos, blockState);
            }
        }
    }
}
