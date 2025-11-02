package com.hoshino.cti.Items.Wine;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Unknown_Wine extends BowlFoodItem {
    public Unknown_Wine(Properties properties) {
        super(properties);
    }

    private static void applyMobEffect(LivingEntity livingEntity, Potion potion) {
        for (MobEffectInstance effectInstance : potion.getEffects()) {
            if (effectInstance.getEffect().isInstantenous()) {
                effectInstance.getEffect().applyInstantenousEffect(livingEntity, livingEntity, livingEntity, 1000, 2);
            } else {
                livingEntity.addEffect(new MobEffectInstance(effectInstance));
            }
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = entity.eat(level, stack);
        if (!level.isClientSide) {
            Collection<Potion> potions = ForgeRegistries.POTIONS.getValues();
            potions.stream().skip(level.random.nextInt(potions.size())).findFirst().ifPresent(potion -> applyMobEffect(entity, potion));
        }
        return result;
    }
}
