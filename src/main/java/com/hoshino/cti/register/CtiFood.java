package com.hoshino.cti.register;

import cofh.core.init.CoreMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class CtiFood {
    public static final FoodProperties COLD_GOBBERWINE = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(CoreMobEffects.COLD_RESISTANCE.get(), 12000, 2), 1).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 12000, 0), 1).effect(() ->
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 12000, 0), 1).saturationMod(1).nutrition(10).alwaysEat().build();
}
