package com.hoshino.cti.util;

import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class EntityUtil {
    public static boolean isAntiStun(LivingEntity living) {
        ItemStack stack = living.getItemBySlot(EquipmentSlot.HEAD);
        if (stack.getItem() instanceof IModifiable) {
            return ToolStack.from(stack).getModifierLevel(CtiModifiers.anti_stun_goggles.get()) > 0;
        }
        return false;
    }

    public static void homingToward(Entity projectile, Entity target, float strength, float baseRadius){
        double velocity = projectile.getDeltaMovement().length();
        float distance = projectile.distanceTo(target);
        Vec3 movementDirection = projectile.getDeltaMovement().normalize().scale(1.0f/(1+strength));
        Vec3 chasingAccelerate = new Vec3(target.getX()-projectile.getX(), target.getY()+target.getBbHeight()*0.5-projectile.getY()-projectile.getBbHeight()*0.5,target.getZ()-projectile.getZ()).normalize().scale(1+baseRadius/distance);
        Vec3 newMovement = movementDirection.add(chasingAccelerate).normalize().scale(velocity);
        projectile.setDeltaMovement(newMovement);
    }
}
