package com.hoshino.cti.mixin.MixinForPlanets.Powah;

import com.hoshino.cti.util.DimensionConstants;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.block.solar.SolarTile;
import owmii.powah.block.thermo.ThermoTile;
import owmii.powah.lib.block.AbstractEnergyProvider;

@Mixin(remap = false, value = AbstractEnergyProvider.class)
public class AbstractEnergyProviderMixin {
    @Inject(at = @At(value = "RETURN"), cancellable = true, method = "getGeneration")
    public void getGeneration(CallbackInfoReturnable<Long> cir) {
        BlockEntity entity = (BlockEntity) (Object) this;
        Level level = entity.getLevel();
        long value = cir.getReturnValueJ();
        if (level != null) {
            if (entity instanceof SolarTile) {
                if (level.dimension().equals(DimensionConstants.INFERNAL)) {
                    cir.setReturnValue((long) (value * 3.5f));
                } else if (level.dimension().equals(DimensionConstants.MERCURY)) {
                    cir.setReturnValue((long) (value * 1.4f));
                }
            } else if (entity instanceof ThermoTile) {
                if (level.dimension().equals(DimensionConstants.URANUS)) {
                    cir.setReturnValue((long) (value * 1.5f));
                } else if (level.dimension().equals(DimensionConstants.GLACIO)) {
                    cir.setReturnValue((long) (value * 1.15f));
                }
            } else if (entity instanceof ReactorTile) {
                if (level.dimension().equals(DimensionConstants.IONIZED_GLACIO)) {
                    cir.setReturnValue((long) (value * 2.25f));
                }
            }
        }
    }
}
