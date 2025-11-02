package com.hoshino.cti.mixin.TwiMixin;

import com.hoshino.cti.util.TwiAdvanceID;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.entity.EnforcedHomePoint;
import twilightforest.entity.boss.Hydra;

@Mixin(Hydra.class)
public abstract class HydraMixin extends Mob implements Enemy, EnforcedHomePoint {

    @Shadow(remap = false)
    protected abstract int countActiveHeads();

    protected HydraMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "hurt", at = @At("RETURN"), cancellable = true)
    private void s(DamageSource src, float damage, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.hurt(src, damage));
    }
    @ModifyArg(method = "attackEntityFromPart",at = @At(value = "INVOKE", target = "Ljava/lang/Math;round(F)I"),remap = false)
    private float setModify(float a){
        return a * 4;
    }
}
