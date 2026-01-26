package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ZirconiumFaucetBlockEntity extends ConfigurableFaucetBlockEntity {
    public ZirconiumFaucetBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.ZR_ALLOY_FAUCET.get(), pos, state);
    }
}
