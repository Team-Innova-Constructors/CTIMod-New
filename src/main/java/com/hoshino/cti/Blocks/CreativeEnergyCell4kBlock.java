package com.hoshino.cti.Blocks;

import appeng.block.AEBaseEntityBlock;
import appeng.helpers.AEMaterials;
import com.hoshino.cti.Blocks.BlockEntity.ae2.CreativeEnergyCell4kBE;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CreativeEnergyCell4kBlock extends AEBaseEntityBlock<CreativeEnergyCell4kBE> {
    public CreativeEnergyCell4kBlock() {
        super(defaultProps(AEMaterials.GLASS));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CreativeEnergyCell4kBE(pPos,pState);
    }
}
