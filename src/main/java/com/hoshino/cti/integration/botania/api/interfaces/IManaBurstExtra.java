package com.hoshino.cti.integration.botania.api.interfaces;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;

public interface IManaBurstExtra {
    IntOpenHashSet cti$getHitEntityIdList();
    void cti$addToHitList(Entity entity);
    void cti$clearHitList();
    float cti$getBaseDamage();
    void cti$setBaseDamage(float baseDamage);
    int cti$getPerConsumption();
    void cti$setPerConsumption(int i);
    int cti$getPerBlockConsumption();
    void cti$setPerBlockConsumption(int i);
    int cti$getGeneration();
    void cti$setGeneration(int i);
    float cti$getDamageModifier();
    void cti$setDamageModifier(float f);

    default void addBaseDamage(float amount){
        this.cti$setBaseDamage(this.cti$getBaseDamage()+amount);
    }
    default void addEntityPerConsumption(int amount){
        this.cti$setPerConsumption(this.cti$getPerConsumption()+amount);
    }
    default void addBlockPerConsumption(int amount){
        this.cti$setPerBlockConsumption(this.cti$getPerBlockConsumption()+amount);
    }
    default void addGeneration(int amount){
        this.cti$setGeneration(this.cti$getGeneration()+amount);
    }
    default void addDamageModifier(float amount){
        this.cti$setDamageModifier(this.cti$getDamageModifier()+amount);
    }
}
