package com.hoshino.cti.util.tier;


import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class Roxy implements Tier {
    public static Tier instance = new Roxy();

    @Override
    public int getUses() {
        return 12;
    }

    @Override
    public float getSpeed() {
        return 12;
    }

    @Override
    public float getAttackDamageBonus() {
        return 12;
    }

    @Override
    public int getLevel() {
        return 12;
    }

    @Override
    public int getEnchantmentValue() {
        return 12;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

}
