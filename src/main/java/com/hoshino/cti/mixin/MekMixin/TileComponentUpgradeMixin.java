package com.hoshino.cti.mixin.MekMixin;

import mekanism.common.tile.component.TileComponentUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = TileComponentUpgrade.class)
public class TileComponentUpgradeMixin {
    @Inject(at = @At("HEAD"), method = "tickServer", cancellable = true)
    private void cancleInstal(CallbackInfo ci) {
        ci.cancel();
    }
}
