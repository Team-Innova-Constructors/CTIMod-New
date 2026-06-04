package com.hoshino.cti.mixin.TwiMixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.UrGhast;
import twilightforest.entity.monster.CarminiteGhastguard;
import twilightforest.entity.monster.CarminiteGhastling;

@Mixin(UrGhast.class)
public abstract class UrGhastMixin extends CarminiteGhastguard {


    public UrGhastMixin(EntityType<? extends CarminiteGhastguard> type, Level world) {
        super(type, world);
    }

    @ModifyVariable(method = "hurt", at = @At(value = "INVOKE", target = "Ltwilightforest/entity/boss/UrGhast;getHealth()F", ordinal = 0), index = 2, argsOnly = true)
    private float changeParam(float value){
        return value * 1.5f;
    }
    @Inject(method = "spawnGhastsAtTraps",at = @At("HEAD"), cancellable = true,remap = false)
    private void preventSpawn(CallbackInfo ci){
        var entityList=this.getLevel().getEntitiesOfClass(CarminiteGhastling.class,this.getBoundingBox().inflate(30));
        if(entityList.size()>15){
            ci.cancel();
        }
    }
}
