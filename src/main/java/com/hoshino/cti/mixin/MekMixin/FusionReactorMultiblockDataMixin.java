package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.Coord4D;
import mekanism.api.MekanismAPI;
import mekanism.api.heat.IHeatCapacitor;
import mekanism.generators.common.content.fusion.FusionReactorMultiblockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FusionReactorMultiblockData.class,remap = false)
public abstract class FusionReactorMultiblockDataMixin {
    @Shadow
    public abstract void setBurning(boolean burn);

    @Shadow
    private AABB deathZone;

    @Shadow
    public IHeatCapacitor heatCapacitor;

    @Shadow
    public abstract double getPlasmaTemp();

    @Shadow
    private double lastCaseTemperature;

    @Shadow
    public double plasmaTemperature;

    @Shadow
    private double lastPlasmaTemperature;

    @Shadow
    private long lastBurned;

    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    public void highTempFailure(Level world, CallbackInfoReturnable<Boolean> cir){
        var data = (FusionReactorMultiblockData)(Object) this;
        if (getPlasmaTemp()<-1E6||getPlasmaTemp()>1.5E12){
            setBurning(false);
            MekanismAPI.getRadiationManager().radiate(new Coord4D(new BlockPos(deathZone.getCenter()),world),Math.abs(this.plasmaTemperature));
            data.heatCapacitor.setHeat(0);
            this.plasmaTemperature = 0;
            this.lastCaseTemperature = 0;
            this.lastPlasmaTemperature = 0;
            this.lastBurned = 0;
            cir.cancel();
        }
    }
}
