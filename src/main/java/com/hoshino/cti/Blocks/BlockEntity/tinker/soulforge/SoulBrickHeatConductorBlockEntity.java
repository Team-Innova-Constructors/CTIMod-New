package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.Blocks.BlockEntity.tinker.ValveBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.heat.HeatBehaviour;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.heat.HeatExchangerLogicConstant;
import me.desht.pneumaticcraft.common.heat.HeatExchangerLogicTicking;
import me.desht.pneumaticcraft.common.heat.HeatExchangerManager;
import me.desht.pneumaticcraft.common.heat.behaviour.HeatBehaviourManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.BlockEntityHelper;
import slimeknights.tconstruct.smeltery.block.entity.component.SmelteryInputOutputBlockEntity;

import java.util.ArrayList;
import java.util.List;

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

    private List<IHeatExchangerLogic> heatExchangers = new ArrayList<>();
    private boolean initialized = false;

    public SoulBrickHeatConductorBlockEntity(BlockPos pos, BlockState state) {
        super(CtiBlockEntityType.SOUL_BRICK_HEAT_CONDUCTOR.get(), pos, state,
                PNCCapabilities.HEAT_EXCHANGER_CAPABILITY, EMPTY);
    }

    @Nullable
    public static <CAST extends SoulBrickHeatConductorBlockEntity, RET extends BlockEntity> BlockEntityTicker<RET> getTicker(Level level, BlockEntityType<RET> check, BlockEntityType<CAST> casting) {
        if (level.isClientSide) return null;
        return BlockEntityHelper.castTicker(check, casting, SERVER_TICKER);
    }

    public static BlockEntityTicker<SoulBrickHeatConductorBlockEntity> SERVER_TICKER = (level, pos, state, blockEntity)->blockEntity.serverTick(level,pos,state,blockEntity);

    private void serverTick(Level level, BlockPos pos, BlockState state, SoulBrickHeatConductorBlockEntity blockEntity) {
        if (!this.initialized){
            initializeHeatExchangers(level,pos);
            this.initialized = true;
        }
        var masterPos = blockEntity.getMasterPos();
        if (masterPos!=null&&level.getBlockEntity(masterPos)!=null){
            var controller = level.getBlockEntity(masterPos);
            if (controller instanceof SoulForgeControllerBlockEntity entity){
                var heatExchanger = entity.getHeatExchanger();
                this.heatExchangers.forEach(logic->
                        HeatExchangerLogicTicking.exchange(heatExchanger,logic));
            }
        }
    }

    public void initializeHeatExchangers(Level level,BlockPos pos){
        this.heatExchangers.clear();
        for (var direction:Direction.values()){
            var posToCheck = pos.relative(direction);
            HeatExchangerManager.getInstance().getLogic(level,posToCheck,direction.getOpposite()).ifPresent(this.heatExchangers::add);
        }
    }

}