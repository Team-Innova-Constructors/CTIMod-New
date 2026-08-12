package com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge;

import com.hoshino.cti.Blocks.BlockEntity.tinker.ValveBlockEntity;
import com.hoshino.cti.register.CtiBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 熔魂炉专用气阀：与 {@link ValveBlockEntity} 行为一致（把控制器的
 * {@code IAirHandlerMachine} 透传到自身朝外的一面），但归属于独立的方块实体类型，
 * 且被 {@link SoulForgeMultiblock} 显式接受为结构壁，因此可作为熔魂炉的抽真空接口。
 */
public class SoulValveBlockEntity extends ValveBlockEntity {
    public SoulValveBlockEntity(BlockPos pos, BlockState state) {
        this(CtiBlockEntityType.SOUL_VALVE.get(), pos, state);
    }

    protected SoulValveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}