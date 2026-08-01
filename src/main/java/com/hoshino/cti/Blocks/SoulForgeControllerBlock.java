package com.hoshino.cti.Blocks;

import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulForgeControllerBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.smeltery.block.controller.HeatingControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 熔魂炉控制器方块。
 */
public class SoulForgeControllerBlock extends HeatingControllerBlock {
    public SoulForgeControllerBlock(Properties builder) {
        super(builder);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootContext.Builder pBuilder) {
        return List.of(new ItemStack(this.asItem()));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.cti.soul_forge_controller").withStyle(ChatFormatting.GRAY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new SoulForgeControllerBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> type) {
        return HeatingStructureBlockEntity.getTicker(pLevel, type, CtiBlockEntityType.SOUL_FORGE.get());
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        BlockEntityHelper.get(SoulForgeControllerBlockEntity.class, worldIn, pos).ifPresent(SoulForgeControllerBlockEntity::updateStructure);
    }

    @Override
    @Deprecated
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!newState.is(this)) {
            BlockEntityHelper.get(SoulForgeControllerBlockEntity.class, worldIn, pos).ifPresent(SoulForgeControllerBlockEntity::invalidateStructure);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
        if (state.getValue(ACTIVE)) {
            double x = pos.getX() + 0.5D;
            double y = (double) pos.getY() + (rand.nextFloat() * 6F + 2F) / 16F;
            double z = pos.getZ() + 0.5D;
            double frontOffset = 0.52D;
            double sideOffset = rand.nextDouble() * 0.6D - 0.3D;
            spawnFireParticles(world, state, x, y, z, frontOffset, sideOffset, ParticleTypes.SOUL_FIRE_FLAME);
        }
    }

    @Deprecated
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (state.getValue(IN_STRUCTURE)) {
            return state;
        }
        return super.rotate(state, rotation);
    }

    @Deprecated
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (state.getValue(IN_STRUCTURE)) {
            return state;
        }
        return super.mirror(state, mirror);
    }
}
