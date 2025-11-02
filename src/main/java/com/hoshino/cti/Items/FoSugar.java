package com.hoshino.cti.Items;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FoSugar extends Item {
    public FoSugar(Properties pProperties, MobEffect effect) {
        super(pProperties.food(new FoodProperties.Builder()
                .alwaysEat()
                .fast()
                .nutrition(4)
                .saturationMod(1.5f)
                .effect(()->new MobEffectInstance(effect,3600,0),1)
                .build()
        ));
    }
}
