package com.hoshino.cti.mixin;

import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.hoshino.cti.util.method.GetModifierLevel.EquipHasModifierlevel;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private Entity entity;

    @Inject(at = @At("HEAD"),method = "getFluidInCamera", cancellable = true)
    private void cancelFluid(CallbackInfoReturnable<FogType> cir){
        Entity entity = this.entity;
        if (entity instanceof LivingEntity living&&EquipHasModifierlevel(living, CtiModifiers.PHOENIX.getId())){
            cir.setReturnValue(FogType.NONE);
        }
    }
}
