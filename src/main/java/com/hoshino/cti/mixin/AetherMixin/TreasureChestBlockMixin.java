package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.block.dungeon.TreasureChestBlock;
import com.aetherteam.aether.blockentity.TreasureChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;
@Mixin(TreasureChestBlock.class)
public abstract class TreasureChestBlockMixin extends AbstractChestBlock<TreasureChestBlockEntity>{
    protected TreasureChestBlockMixin(Properties pProperties, Supplier<BlockEntityType<? extends TreasureChestBlockEntity>> pBlockEntityType) {
        super(pProperties, pBlockEntityType);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState stateOther, boolean flag) {
        if (!state.is(stateOther.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TreasureChestBlockEntity treasureChest) {
                if (!treasureChest.getLocked()) {
                    Containers.dropContents(level, pos, treasureChest);
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            level.removeBlockEntity(pos);
        }
    }
}
