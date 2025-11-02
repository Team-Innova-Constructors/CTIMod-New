package com.hoshino.cti.Blocks.BlockEntity.Ports;

import fr.frinn.custommachinery.api.machine.MachineTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BasicPort extends BlockEntity {
    public BasicPort(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState,PortType portType,TransferType transferType) {
        super(pType, pPos, pBlockState);
        this.portType = portType;
        this.transferType =transferType;
    }
    public BlockPos controllerPos;
    public final PortType portType;
    public final TransferType transferType;


    public BlockEntityTicker<BasicPort> getTicker(){
        return (pLevel, pPos, pState, pBlockEntity) -> tick();
    }

    public void tick(){
        if (this.controllerPos!=null&&this.level!=null&&!(this.level.getBlockEntity(this.controllerPos) instanceof MachineTile)) this.controllerPos=null;
    }
}
