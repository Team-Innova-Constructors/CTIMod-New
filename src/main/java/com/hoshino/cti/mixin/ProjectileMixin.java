package com.hoshino.cti.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public class ProjectileMixin {
    @Inject(method = "shoot",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;horizontalDistance()D"))
    private void modifyArrowDamage(CallbackInfo ci){
        if ((Entity)(Object) this instanceof AbstractArrow arrow){
            arrow.setBaseDamage(arrow.getBaseDamage()*arrow.getDeltaMovement().length());
        }
    }
}
