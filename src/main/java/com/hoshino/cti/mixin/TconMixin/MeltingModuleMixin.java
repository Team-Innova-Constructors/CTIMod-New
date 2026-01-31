package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.util.ICtiMeltingModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule;

import static slimeknights.tconstruct.smeltery.block.entity.module.MeltingModule.NO_SPACE;

@Mixin(value = MeltingModule.class,remap = false)
public abstract class MeltingModuleMixin implements ICtiMeltingModule {

    @Shadow private int currentTime;

    @Shadow public abstract boolean canHeatItem(int temperature);

    @Shadow private int requiredTime;

    @Shadow protected abstract boolean onItemFinishedHeating();

    @Shadow protected abstract void resetRecipe();

    @Override
    public void cti$heatItem(int temperature,float speedMod) {
        if (this.currentTime == NO_SPACE || this.canHeatItem(temperature)) {
            // if we are done, cook item
            if (currentTime == NO_SPACE || currentTime >= this.requiredTime) {
                if (this.onItemFinishedHeating()) {
                    this.resetRecipe();
                }
            } else {
                currentTime += (int) (temperature * speedMod / 100);
            }
        }
    }
}
