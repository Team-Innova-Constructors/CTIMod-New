package com.hoshino.cti.util;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;


public class EntityInRangeUtil {
    public static ToDoubleFunction<? super Entity> toManhattanDistance(Entity entity){
        return value -> Math.abs( value.getX()-entity.getX()+value.getY()-entity.getY());
    }
    public static ToDoubleFunction<? super Entity> toReverseManhattanDistance(Entity entity){
        return value -> -Math.abs( value.getX()-entity.getX()+value.getY()-entity.getY());
    }
    public static ToDoubleFunction<? super Entity> toActualDistance(Entity entity){
        return e -> e.distanceTo(entity);
    }
    public static Entity getNearestEntity(@NotNull Entity centerEntity, float range,@NotNull IntOpenHashSet ignoreEntityIds, @NotNull Predicate<Entity> predicate){
        List<Entity> list = centerEntity.level.getEntitiesOfClass(Entity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toActualDistance(centerEntity)));
        for (Entity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&predicate.test(entity)&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
    public static LivingEntity getNearestLivingEntity(@NotNull Entity centerEntity, float range,@NotNull IntOpenHashSet ignoreEntityIds, @NotNull Predicate<Entity> predicate){
        List<LivingEntity> list = centerEntity.level.getEntitiesOfClass(LivingEntity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toActualDistance(centerEntity)));
        for (LivingEntity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&predicate.test(entity)&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
    public static LivingEntity getNearestNotFriendlyLivingEntity(@NotNull Entity centerEntity, float range, @NotNull IntOpenHashSet ignoreEntityIds, @NotNull Predicate<Entity> predicate){
        List<LivingEntity> list = centerEntity.level.getEntitiesOfClass(LivingEntity.class,new AABB(centerEntity.blockPosition()).inflate(range));
        list.sort(Comparator.comparingDouble(toActualDistance(centerEntity)));
        for (LivingEntity entity:list){
            if (!ignoreEntityIds.contains(entity.getId())&&predicate.test(entity)&&!entity.getType().getCategory().isFriendly()&&entity!=centerEntity){
                return entity;
            }
        }
        return null;
    }
}
