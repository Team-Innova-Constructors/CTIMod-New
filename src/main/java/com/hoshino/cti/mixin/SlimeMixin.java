package com.hoshino.cti.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Slime.class)
public abstract class SlimeMixin extends Mob {
    protected SlimeMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }
    @Inject(method = "remove",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),locals = LocalCapture.CAPTURE_FAILSOFT)
    public void remove(RemovalReason p_149847_, CallbackInfo ci, int i, Component component, boolean flag, float f, int j, int k, int l, float f1, float f2, Slime slime){
        int iterationCount=this.getPersistentData().getInt("cti:iteration");
        slime.getPersistentData().putInt("cti:iteration",iterationCount+1);
    }

}
