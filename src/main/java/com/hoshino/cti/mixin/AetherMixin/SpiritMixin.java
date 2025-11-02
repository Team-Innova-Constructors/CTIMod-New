package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.entity.monster.dungeon.boss.SunSpirit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SunSpirit.class)
public abstract class SpiritMixin extends PathfinderMob{

    @Shadow(remap = false) public abstract void setFrozen(boolean frozen);

    protected SpiritMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    private int cti$freezeTick;
    @Inject(method = "isInvulnerableTo",at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if(cti$freezeTick >0){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "tick",at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        cti$freezeTick--;
    }
    @Inject(method = "hurt",at = @At("HEAD"))
    private void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(source.getMsgId().equals("aether.ice_crystal")){
            cti$freezeTick =200;
        }
    }
    @Inject(method = "hurt",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    private void spawn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        boolean shouldSpawn=source.getMsgId().equals("aether.ice_crystal");
        if(!shouldSpawn) cir.setReturnValue(true);
    }
    @ModifyArg(method = "customServerAiStep",at = @At(value = "INVOKE", target = "Lcom/aetherteam/aether/entity/monster/dungeon/boss/SunSpirit;setFrozen(Z)V",remap = false))
    private boolean set(boolean frozen){
        return cti$freezeTick>0;
    }

}
