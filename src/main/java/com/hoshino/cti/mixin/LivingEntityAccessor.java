package com.hoshino.cti.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor extends EntityAccessor{
    @Accessor
    int getAttackStrengthTicker();
    @Accessor
    void setNoActionTime(int time);
    @Accessor
    float getLastHurt();
    @Accessor
    void setLastHurt(float lastHurt);
    @Invoker("hurtHelmet")
    void hurtHelmet(DamageSource pDamageSource, float pDamageAmount);
    @Accessor
    void setLastHurtByPlayerTime(int time);
    @Accessor
    void setLastHurtByPlayer(Player player);
    @Invoker("getDeathSound")
    SoundEvent getDeathSound();
    @Invoker("getSoundVolume")
    float getSoundVolume();
    @Invoker("playHurtSound")
    void playHurtSound(DamageSource pSource);
    @Accessor
    void setLastDamageSource(DamageSource source);
    @Accessor
    void setLastDamageStamp(long l);
    @Accessor
    boolean getDead();
    @Accessor
    void setDead(boolean dead);
    @Accessor
    int getDeathScore();
    @Invoker("dropAllDeathLoot")
    void dropAllDeathLoot(DamageSource pSource);
    @Invoker("createWitherRose")
    void createWitherRose(LivingEntity living);


}
