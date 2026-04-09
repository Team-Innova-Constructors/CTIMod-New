package com.hoshino.cti.Blocks;

import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.ClientTickingBlockEntity;
import appeng.blockentity.ServerTickingBlockEntity;
import appeng.blockentity.misc.InterfaceBlockEntity;
import appeng.menu.locator.MenuLocators;
import appeng.util.InteractionUtil;
import com.hoshino.cti.Blocks.BlockEntity.ManaInterfaceBE;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ManaInterfaceBlock extends AEBaseEntityBlock<ManaInterfaceBE> {
    public ManaInterfaceBlock() {
        super(defaultProps(Material.METAL));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ManaInterfaceBE(blockPos,blockState);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        return List.of( new ItemStack(this.asItem()));
    }

    public InteractionResult onActivated(Level level, BlockPos pos, Player p, InteractionHand hand, @javax.annotation.Nullable ItemStack heldItem, BlockHitResult hit) {
        if (InteractionUtil.isInAlternateUseMode(p)) {
            return InteractionResult.PASS;
        } else {
            InterfaceBlockEntity be = this.getBlockEntity(level, pos);
            if (be != null) {
                if (!level.isClientSide) {
                    be.openMenu(p, MenuLocators.forBlockEntity(be));
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }
}
