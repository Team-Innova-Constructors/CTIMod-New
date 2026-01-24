package com.hoshino.cti.Blocks.BlockEntity;

import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class HepatizonFaucetBlockEntity extends ConfigurableFaucetBlockEntity {
    public HepatizonFaucetBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.HEPATIZON_FAUCET.get(), pos, state);
    }
}
