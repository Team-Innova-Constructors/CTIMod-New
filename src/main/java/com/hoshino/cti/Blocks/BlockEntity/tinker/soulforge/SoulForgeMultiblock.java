package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;

/**
 * 熔魂炉的多方块结构识别：有底、有边框、有顶。
 * 顶面（含边框）只接受 {@code SOUL_BRICK_HEAT_CONDUCTOR}（魂砖导热件）。
 * 侧面/底面/边框接受原版 {@code tconstruct:smeltery} 系列墙块以及
 * 自定义的魂砖导热件、魂碳气阀。
 */
public class SoulForgeMultiblock extends HeatingStructureMultiblock<SoulForgeControllerBlockEntity> {
    public SoulForgeMultiblock(SoulForgeControllerBlockEntity parent) {
        // 有底，有边框，有顶 —— 与原版冶炼炉不同，熔魂炉封顶
        super(parent, true, true, true);
    }

    @Override
    protected boolean isValidBlock(Block block) {
        return block.builtInRegistryHolder().is(TinkerTags.Blocks.SMELTERY);
    }

    @Override
    protected boolean isValidFloor(Block block) {
        return block.builtInRegistryHolder().is(TinkerTags.Blocks.SMELTERY_FLOOR);
    }

    @Override
    protected boolean isValidTank(Block block) {
        return block.builtInRegistryHolder().is(TinkerTags.Blocks.SMELTERY_TANKS);
    }

    @Override
    protected boolean isValidWall(Block block) {
        return block.builtInRegistryHolder().is(TinkerTags.Blocks.SMELTERY_WALL)
                || block == CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get()
                || block == CtiBlock.SOUL_VALVE.get();
    }

    @Override
    protected boolean isValidBlock(Level world, BlockPos pos, CuboidSide side, boolean isFrame) {
        // 顶面（含边框）只允许魂砖导热件。
        // 先走父类逻辑，确保 servant/in-structure 追踪、tank 收集等行为不被绕过，
        // 再按位置把方块限制到 SOUL_BRICK_HEAT_CONDUCTOR。
        if (side == CuboidSide.CEILING) {
            Block block = world.getBlockState(pos).getBlock();
            if (block != CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get()) {
                return false;
            }
            return super.isValidBlock(world, pos, side, isFrame);
        }
        return super.isValidBlock(world, pos, side, isFrame);
    }
}