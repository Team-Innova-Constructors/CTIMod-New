package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.smeltery.block.component.SearedTankBlock;
import slimeknights.tconstruct.smeltery.block.entity.multiblock.HeatingStructureMultiblock;

/**
 * 熔魂炉的多方块结构识别：有底、有边框、有顶。
 * 顶面（含边框）只接受 {@code SOUL_BRICK_HEAT_CONDUCTOR}（魂砖导热件）。
 * 侧面/底面/边框接受原版 {@code tconstruct:smeltery} 系列墙块以及
 * 自定义的魂砖导热件、魂碳气阀、魂砖、魂玻璃、魂量器。
 */
public class SoulForgeMultiblock extends HeatingStructureMultiblock<SoulForgeControllerBlockEntity> {
    public SoulForgeMultiblock(SoulForgeControllerBlockEntity parent) {
        // 有底，有边框，有顶 —— 与原版冶炼炉不同，熔魂炉封顶
        super(parent, true, true, true);
    }

    @Override
    protected boolean isValidBlock(Block block) {
        return block == CtiBlock.SOUL_FORGE_BRICK.get()
                || block == CtiBlock.SOUL_FORGE_GLASS.get()
                || block == CtiBlock.SOUL_FORGE_DRAIN.get()
                || block == CtiBlock.SOUL_FORGE_DUCT.get()
                || block == CtiBlock.SOUL_FORGE_CHUTE.get()
                || block == CtiBlock.SOUL_VALVE.get()
                || block == CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get()
                || isValidTank(block);
    }

    @Override
    protected boolean isValidFloor(Block block) {
        return block == CtiBlock.SOUL_FORGE_BRICK.get();
    }

    @Override
    protected boolean isValidTank(Block block) {
        return block == CtiBlock.SOUL_FORGE_TANK.get(SearedTankBlock.TankType.FUEL_GAUGE)
                || block == CtiBlock.SOUL_FORGE_TANK.get(SearedTankBlock.TankType.INGOT_GAUGE);
    }

    @Override
    protected boolean isValidWall(Block block) {
        return block == CtiBlock.SOUL_VALVE.get()
                || block == CtiBlock.SOUL_FORGE_BRICK.get()
                || block == CtiBlock.SOUL_FORGE_GLASS.get()
                || block == CtiBlock.SOUL_FORGE_DRAIN.get()
                || block == CtiBlock.SOUL_FORGE_DUCT.get()
                || block == CtiBlock.SOUL_FORGE_CHUTE.get()
                || block == CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get()
                || isValidTank(block);
    }

    @Override
    protected boolean isValidBlock(Level world, BlockPos pos, CuboidSide side, boolean isFrame) {
        // 顶面（含边框）只允许魂砖导热件。
        if (side == CuboidSide.CEILING) {
            Block block = world.getBlockState(pos).getBlock();
            return block == CtiBlock.SOUL_BRICK_HEAT_CONDUCTOR.get();
        }
        return super.isValidBlock(world, pos, side, isFrame);
    }
}