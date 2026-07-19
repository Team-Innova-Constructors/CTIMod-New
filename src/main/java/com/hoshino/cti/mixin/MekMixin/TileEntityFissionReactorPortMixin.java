package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.IContentsListener;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.lib.multiblock.IMultiblockEjector;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorCasing;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorPort;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = TileEntityFissionReactorPort.class,remap = false)
public abstract class TileEntityFissionReactorPortMixin extends TileEntityFissionReactorCasing implements IMultiblockEjector {
    public TileEntityFissionReactorPortMixin(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    /**
     * @author
     * @reason 不让裂变堆接收热量
     */
    @Overwrite
    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        return null;
    }
}
