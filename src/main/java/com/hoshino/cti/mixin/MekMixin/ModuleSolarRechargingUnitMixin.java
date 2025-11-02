package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.gear.IModule;
import mekanism.api.math.FloatingLong;
import mekanism.common.config.MekanismConfig;
import mekanism.common.util.WorldUtils;
import mekanism.generators.common.content.gear.mekasuit.ModuleSolarRechargingUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(remap = false, value = ModuleSolarRechargingUnit.class)
public class ModuleSolarRechargingUnitMixin {
    @Final
    @Shadow
    private static FloatingLong RAIN_MULTIPLIER;

    /**
     * @author EtSH_C2H6S
     * @reason 修复太阳能模组崩溃
     */
    @Overwrite
    public void tickServer(IModule<ModuleSolarRechargingUnit> module, Player player) {
        IEnergyContainer energyContainer = module.getEnergyContainer();
        if (energyContainer != null && !energyContainer.getNeeded().isZero()) {
            BlockPos pos = new BlockPos(player.getX(), player.getEyeY() + 0.2, player.getZ());
            if (WorldUtils.canSeeSun(player.level, pos)) {
                Biome b = player.level.getBiomeManager().getBiome(pos).value();
                boolean needsRainCheck = b.getPrecipitation() != Biome.Precipitation.NONE;
                float tempEff = 0.3F * (0.8F - b.getBaseTemperature());
                tempEff = Math.max(tempEff, 0);
                float humidityEff = needsRainCheck ? -0.3F * b.getDownfall() : 0.0F;
                FloatingLong peakOutput = MekanismConfig.gear.mekaSuitSolarRechargingRate.get().multiply(1.0F + tempEff + humidityEff);
                float brightness = WorldUtils.getSunBrightness(player.level, 1.0F);
                FloatingLong production = peakOutput.multiply(brightness);
                if (needsRainCheck && (player.level.isRaining() || player.level.isThundering())) {
                    production = production.timesEqual(RAIN_MULTIPLIER);
                }
                energyContainer.insert(production.multiply(module.getInstalledCount()), Action.EXECUTE, AutomationType.MANUAL);
            }
        }
    }
}
