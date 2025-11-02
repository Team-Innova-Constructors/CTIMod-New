package com.hoshino.cti.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Vec3Helper {
    public static @Nullable <T extends Entity> T getPointedEntity(LivingEntity viewer, Level level, int dist, Class<T> tClass, Predicate<? super T> nearByFilter, Predicate<? super T> passFilter) {
        var playerEyePosition = viewer.getEyePosition(1F);
        var playerLook = viewer.getViewVector(1F);
        var eyeSight = playerEyePosition.add(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist);
        var nearByBox = viewer.getBoundingBox().expandTowards(playerLook.x * dist, playerLook.y * dist, playerLook.z * dist).inflate(1.0F, 1.0F, 1.0F);
        T pointedEntity = null;
        double currentDist = dist;
        List<T> nearbyEntities = level.getEntitiesOfClass(tClass, nearByBox, nearByFilter);
        for (T nearbyEntity : nearbyEntities) {
            if (nearbyEntity == viewer) continue;
            if (passFilter.test(nearbyEntity)) continue;
            AABB axisAlignedBB = nearbyEntity.getBoundingBox().inflate(nearbyEntity.getPickRadius());
            Optional<Vec3> optional = axisAlignedBB.clip(playerEyePosition, eyeSight);
            if (axisAlignedBB.contains(playerEyePosition)) {
                pointedEntity = nearbyEntity;
                currentDist = 0;
            } else if (optional.isPresent()) {
                double distanceTo = playerEyePosition.distanceTo(optional.get());
                if (distanceTo < currentDist) {
                    currentDist = distanceTo;
                    pointedEntity = nearbyEntity;
                }
            }
        }
        return pointedEntity;
    }
}
