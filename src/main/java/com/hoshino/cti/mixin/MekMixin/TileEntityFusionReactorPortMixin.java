package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.IContentsListener;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorPort;
import mekanism.generators.common.tile.fusion.TileEntityFusionReactorBlock;
import mekanism.generators.common.tile.fusion.TileEntityFusionReactorPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = TileEntityFusionReactorPort.class,remap = false)
public abstract class TileEntityFusionReactorPortMixin extends TileEntityFusionReactorBlock {
    public TileEntityFusionReactorPortMixin(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * @author
     * @reason 不让聚变堆换热
     */
    @Overwrite
    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        return null;
    }
}
