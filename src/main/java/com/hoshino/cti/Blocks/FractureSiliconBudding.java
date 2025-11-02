package com.hoshino.cti.Blocks;

import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import slimeknights.tconstruct.fluids.TinkerFluids;

public class FractureSiliconBudding extends AmethystBlock {
    public FractureSiliconBudding(Properties p_151999_) {
        super(p_151999_);
    }

    private static final Direction[] DIRECTIONS = Direction.values();

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
        BlockPos blockpos = pPos.relative(direction);
        BlockState blockstate = pLevel.getBlockState(blockpos);
        int rnd = pRandom.nextInt(300);

        if (blockstate.is(Blocks.AIR) || blockstate.is(Blocks.CAVE_AIR) || blockstate.is(Blocks.VOID_AIR) || blockstate.is(Blocks.WATER)) {
            if (rnd <= 1) {
                Block block = CtiBlock.fracture_silicon.get();
                BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                pLevel.setBlockAndUpdate(blockpos, blockState);
            }
        } else if (blockstate.getBlock() instanceof LiquidBlock) {
            FluidState fluidState = pLevel.getFluidState(blockpos);
            Block block = CtiBlock.fracture_silicon.get();
            if (fluidState.isSource()) {
                if (blockstate.is(Blocks.WATER) && rnd <= 3) {
                    BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                    pLevel.setBlockAndUpdate(blockpos, blockState);
                } else if (blockstate.is(Blocks.LAVA) && rnd <= 30) {
                    BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                    pLevel.setBlockAndUpdate(blockpos, blockState);
                } else if (fluidState.is(TinkerFluids.scorchedStone.get()) || fluidState.is(TinkerFluids.searedStone.get())) {
                    for (Direction direction1 : DIRECTIONS) {
                        BlockState blockState1 = pLevel.getBlockState(pPos.relative(direction1));
                        if (blockState1.getBlock() instanceof LiquidBlock || blockState1.is(Blocks.AIR) || blockState1.is(Blocks.CAVE_AIR) || blockState1.is(Blocks.VOID_AIR)) {
                            BlockState blockState = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction1).setValue(AmethystClusterBlock.WATERLOGGED, blockstate.getFluidState().getType() == Fluids.WATER);
                            pLevel.setBlockAndUpdate(pPos.relative(direction1), blockState);
                        }
                    }
                }
            }
        }
    }
}
