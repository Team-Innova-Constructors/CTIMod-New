package com.hoshino.cti.Blocks;

import com.hoshino.cti.Blocks.BlockEntity.tinker.ZirconiumFaucetBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.smeltery.block.entity.FaucetBlockEntity;

import java.util.List;

public class ZirconiumFaucetBlock extends ConfigurableFaucetBlock {
    public ZirconiumFaucetBlock(Properties properties) {
        super(properties, ()->200000000);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        return List.of(new ItemStack(this.asItem()));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ZirconiumFaucetBlockEntity(pPos,pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> type) {
        return BlockEntityHelper.serverTicker(pLevel,type, CtiBlockEntityType.ZR_ALLOY_FAUCET.get(), FaucetBlockEntity.SERVER_TICKER);
    }
}
