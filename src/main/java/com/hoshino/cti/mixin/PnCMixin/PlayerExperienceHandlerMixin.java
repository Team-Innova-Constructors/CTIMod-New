package com.hoshino.cti.mixin.PnCMixin;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "me.desht.pneumaticcraft.common.block.entity.AerialInterfaceBlockEntity$PlayerExperienceHandler",remap = false)
public abstract class PlayerExperienceHandlerMixin {
    @Shadow protected abstract boolean canFill(Fluid fluid);

    @Inject(method = "drain(Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraftforge/fluids/capability/IFluidHandler$FluidAction;)Lnet/minecraftforge/fluids/FluidStack;",at = @At("HEAD"),cancellable = true)
    public void cancelIfIncorrectFluid(FluidStack resource, IFluidHandler.FluidAction doDrain, CallbackInfoReturnable<FluidStack> cir){
        if (!this.canFill(resource.getFluid())) cir.setReturnValue(FluidStack.EMPTY);
    }
}
