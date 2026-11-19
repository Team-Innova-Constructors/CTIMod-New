package com.hoshino.cti.mixin.MekMixin;

import mekanism.common.capabilities.heat.VariableHeatCapacitor;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.content.fission.FissionReactorMultiblockData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static mekanism.generators.common.content.fission.FissionReactorMultiblockData.*;

@Mixin(value = FissionReactorMultiblockData.class,remap = false)
public abstract class FissionReactorMultiblockDataMixin extends MultiblockData implements IValveHandler {
    @Shadow
    public double reactorDamage;

    @Shadow
    public VariableHeatCapacitor heatCapacitor;

    @Shadow
    public abstract boolean isForceDisabled();

    @Shadow
    abstract void setForceDisable(boolean forceDisable);

    @Shadow
    protected abstract void createMeltdown(Level world);

    public FissionReactorMultiblockDataMixin(BlockEntity tile) {
        super(tile);
    }

    /**
     * @author
     * @reason 让裂变堆的损坏能是负值
     */
    @Overwrite
    private void handleDamage(Level world) {
        double lastDamage = reactorDamage;
        double temp = heatCapacitor.getTemperature();
        if (temp > MIN_DAMAGE_TEMPERATURE) {
            double damageRate = Math.min(temp, MAX_DAMAGE_TEMPERATURE) / (MIN_DAMAGE_TEMPERATURE * 10);
            reactorDamage += damageRate;
        } else if (reactorDamage>0){
            double repairRate = (MIN_DAMAGE_TEMPERATURE - temp) / (MIN_DAMAGE_TEMPERATURE * 100);
            reactorDamage = Math.max(0, reactorDamage - repairRate);
        }
        if (reactorDamage >= MAX_DAMAGE && temp >= MIN_DAMAGE_TEMPERATURE) {
            if (isForceDisabled() && MekanismGeneratorsConfig.generators.fissionMeltdownsEnabled.get()) {
                setForceDisable(false);
                createMeltdown(world);
            } else if (world.random.nextDouble() < (reactorDamage / MAX_DAMAGE) * MekanismGeneratorsConfig.generators.fissionMeltdownChance.get()) {
                if (MekanismGeneratorsConfig.generators.fissionMeltdownsEnabled.get()) {
                    createMeltdown(world);
                } else {
                    setForceDisable(true);
                }
            }
        } else if (reactorDamage < MAX_DAMAGE && temp < MIN_DAMAGE_TEMPERATURE) {
            setForceDisable(false);
        }
        if (reactorDamage != lastDamage) {
            markDirty();
        }
    }

}
