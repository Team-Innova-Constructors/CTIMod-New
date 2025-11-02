package com.hoshino.cti.register;

import cofh.core.init.CoreMobEffects;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import umpaz.brewinandchewin.common.registry.BCEffects;

public class CtiWine {
    public static final FoodProperties COLD_GOBBERWINE = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(CoreMobEffects.COLD_RESISTANCE.get(), 12000, 2), 1).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 12000, 0), 1).effect(() ->
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 12000, 0), 1).saturationMod(1).nutrition(10).alwaysEat().build();
    public static final FoodProperties ETHANOL_ABSOLUTE = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.SLOW_FALLING, 12000, 0), 1).effect(() ->
            new MobEffectInstance(MobEffects.CONFUSION, 6000, 0), 0.99F).effect(() ->
            new MobEffectInstance(MobEffects.BLINDNESS, 6000, 0), 0.99F).effect(() ->
            new MobEffectInstance(MobEffects.DARKNESS, 6000, 0), 0.99F).effect(() ->
            new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 6000, 0), 0.99F).effect(() ->
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6000, 0), 0.99F).effect(() ->
            new MobEffectInstance(MobEffects.POISON, 6000, 2), 0.99F).alwaysEat().build();
    public static final FoodProperties UNKNOWN_WINE = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(BCEffects.TIPSY.get(), 12000, 0), 1).alwaysEat().nutrition(10).saturationMod(1).build();
    public static final FoodProperties cornflower_beer = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.LUCK, 12000, 3), 1).effect(() ->
            new MobEffectInstance(MobEffects.REGENERATION, 12000, 0), 1).saturationMod(1).nutrition(10).alwaysEat().build();
    public static final FoodProperties xuerou_wine = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.REGENERATION, 6000, 1), 0.3F).effect(() ->
            new MobEffectInstance(MobEffects.CONFUSION, 600, 0), 1).effect(() ->
            new MobEffectInstance(MobEffects.POISON, 6000, 0), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.BLINDNESS, 3000, 1), 0.8F).saturationMod(0.5F).nutrition(4).alwaysEat().fast().build();
    public static final FoodProperties mahoushaojiu_wine = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT.get(), 12000, 0), 1F).effect(() ->
            new MobEffectInstance(ModPotions.SPELL_DAMAGE_EFFECT.get(), 12000, 1), 1F).effect(() ->
            new MobEffectInstance(ModPotions.MAGIC_FIND_EFFECT.get(), 6000, 0), 0.5F).effect(() ->
            new MobEffectInstance(ModPotions.RECOVERY_EFFECT.get(), 3600, 1), 0.5F).saturationMod(0.5F).nutrition(2).alwaysEat().fast().build();
    public static final FoodProperties etbeer = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.HUNGER, 12000, 4), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.CONFUSION, 12000, 4), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 12000, 4), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.BLINDNESS, 12000, 4), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.POISON, 12000, 1), 1).saturationMod(1).nutrition(20).fast().alwaysEat().build();
    public static final FoodProperties qdbeer = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 12000, 2), 1F).effect(() ->
            new MobEffectInstance(MobEffects.LUCK, 12000, 1), 1F).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 12000, 2), 1F).effect(() ->
            new MobEffectInstance(MobEffects.SLOW_FALLING, 3000, 0), 1).saturationMod(10).nutrition(2).alwaysEat().build();
    public static final FoodProperties boomwine = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(ModPotions.BLAST_EFFECT.get(), 200, 6), 1F).effect(() ->
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 1), 1F).effect(() ->
            new MobEffectInstance(MobEffects.JUMP, 6000, 2), 1F).effect(() ->
            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 2), 0.8F).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000, 2), 0.3F).saturationMod(0.2F).nutrition(5).alwaysEat().build();
    public static final FoodProperties fishbone_wine = (
            new FoodProperties.Builder()).effect(() ->
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 4), 1F).effect(() ->
            new MobEffectInstance(MobEffects.REGENERATION, 2000, 4), 1F).effect(() ->
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 900, 4), 1F).saturationMod(1F).nutrition(10).alwaysEat().build();
}
