package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.register.CtiBlockEntityType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.heat.HeatExchangerLogicConstant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity;

/**
 * 绞硅导热件：作为熔魂炉结构壁的一部分，将控制器的热交换器
 * 通过能力查询透传到自身朝外的一面，从而与相邻的 PneumaticCraft
 * 热系统方块（如散热器、热管道）进行热交换。
 *
 * <p>自身不持有热交换器，只作为路由：当熔魂炉控制器为 master
 * 时，{@link #getCapability(BlockEntity)} 会返回控制器暴露的
 * {@link IHeatExchangerLogic}（即 {@link SoulForgeControllerBlockEntity} 的热腔）。</p>
 */
public class SoulBrickHeatConductorBlockEntity extends SmelteryInputOutputBlockEntity<IHeatExchangerLogic> {
    /** 用于无 master 时空实现，避免外部能力无效化时出现空指针 */
    private static final IHeatExchangerLogic EMPTY = new HeatExchangerLogicConstant(300, 1);

    public SoulBrickHeatConductorBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.SOUL_BRICK_HEAT_CONDUCTOR.get(), pos, state,
                PNCCapabilities.HEAT_EXCHANGER_CAPABILITY, EMPTY);
    }
}