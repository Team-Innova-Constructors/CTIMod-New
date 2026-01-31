package com.hoshino.cti.Blocks.BlockEntity.tinker;

import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;
import slimeknights.tconstruct.smeltery.block.entity.component.TankBlockEntity;

public class CtiTankBlockEntity extends TankBlockEntity {
    public CtiTankBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.TANK.get(),pos, state,state.getBlock() instanceof ITankBlock tank ? tank
                : TinkerSmeltery.searedTank.get(SearedTankBlock.TankType.FUEL_TANK));
    }
}
