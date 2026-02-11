package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.smeltery.item.TankItemFluidHandler;

@Mixin(value = TankItemFluidHandler.class,remap = false)
public class TankItemFluidHandlerMixin {
    @Shadow @Final private ItemStack container;

    @Inject(method = "getCapability",at = @At("RETURN"), cancellable = true)
    public void cancelIfStacked(Capability<?> cap, Direction side, CallbackInfoReturnable<LazyOptional<?>> cir){
        if (this.container.getCount()>1) cir.setReturnValue(LazyOptional.empty());
    }
}
