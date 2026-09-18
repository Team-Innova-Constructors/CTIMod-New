package com.hoshino.cti.mixin.PnCMixin;

import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "me.desht.pneumaticcraft.common.thirdparty.mekanism.PNC2MekHeatProvider$PNC2MekHeatAdapter",remap = false)
public class PNC2MekHeatAdapterMixin {
    @Inject(method = "handleHeat",at = @At("HEAD"),cancellable = true)
    private void ignoreLowTemp(int i, double amount, Direction direction, CallbackInfo ci){
        if (amount<=0) ci.cancel();
    }
}
