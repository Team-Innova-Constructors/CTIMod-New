package com.hoshino.cti.Blocks.BlockEntity.Ports;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FluidInputPortBE extends BasicPort{
    public FluidInputPortBE(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int capacity) {
        super(pType, pPos, pBlockState, PortType.FLUID, TransferType.INPUT);
    }
}
