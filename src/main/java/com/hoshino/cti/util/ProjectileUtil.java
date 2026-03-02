package com.hoshino.cti.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;


public class ProjectileUtil {
    //一个简单的弹射物追踪方法
    public static void homingToward(Entity projectile, Entity target, float strength, float baseRadius){
        double velocity = projectile.getDeltaMovement().length();
        float distance = projectile.distanceTo(target);
        Vec3 movementDirection = projectile.getDeltaMovement().normalize().scale(1.0f/(1+strength));
        Vec3 chasingAccelerate = new Vec3(target.getX()-projectile.getX(), target.getY()+target.getBbHeight()*0.5-projectile.getY()-projectile.getBbHeight()*0.5,target.getZ()-projectile.getZ()).normalize().scale(1+baseRadius/distance);
        Vec3 newMovement = movementDirection.add(chasingAccelerate).normalize().scale(velocity);
        projectile.setDeltaMovement(newMovement);
    }
}
