package com.hoshino.cti.Blocks.BlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;

public class ConfigurableFaucetBlockEntity extends FaucetBlockEntity {
    public ConfigurableFaucetBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
