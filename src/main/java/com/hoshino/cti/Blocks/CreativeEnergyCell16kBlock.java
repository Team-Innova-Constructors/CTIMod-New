package com.hoshino.cti.Blocks;

import appeng.block.AEBaseEntityBlock;
import appeng.helpers.AEMaterials;
import com.hoshino.cti.Blocks.BlockEntity.ae2.CreativeEnergyCell16kBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreativeEnergyCell16kBlock extends AEBaseEntityBlock<CreativeEnergyCell16kBE> {
    public CreativeEnergyCell16kBlock() {
        super(defaultProps(AEMaterials.GLASS));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CreativeEnergyCell16kBE(pPos,pState);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        return List.of(new ItemStack(this.asItem()));
    }


}
