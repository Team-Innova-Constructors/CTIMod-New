package com.hoshino.cti.Blocks.BlockEntity.tinker.refinery;

import com.hoshino.cti.register.CtiBlock;
import net.minecraft.world.level.block.Block;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;

public class RefineryMultiblock extends HeatingStructureMultiblock<RefineryControllerBlockEntity> {
    public RefineryMultiblock(RefineryControllerBlockEntity parent) {
        super(parent, true, true, true);
    }

    @Override
    protected boolean isValidBlock(Block block) {
        return block== CtiBlock.REFINERY.get();
    }

    @Override
    protected boolean isValidFloor(Block block) {
        return block==CtiBlock.SILICATED_BRICK.get();
    }

    @Override
    protected boolean isValidTank(Block block) {
        return block==CtiBlock.SILICATED_TANK.get(SearedTankBlock.TankType.FUEL_GAUGE)||block==CtiBlock.SILICATED_TANK.get(SearedTankBlock.TankType.INGOT_GAUGE);
    }

    @Override
    protected boolean isValidWall(Block block) {
        return isValidTank(block)||block==CtiBlock.SILICATED_BRICK.get()||block==CtiBlock.SILICATED_GLASS.get()||
                block==CtiBlock.SILICATED_DRAIN.get()||block==CtiBlock.SILICATED_DUCT.get()||block==CtiBlock.SILICATED_CHUTE.get()
                ||block==CtiBlock.SILICATED_VALVE.get();
    }
}
