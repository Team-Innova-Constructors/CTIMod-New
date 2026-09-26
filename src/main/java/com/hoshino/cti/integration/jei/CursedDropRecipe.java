package com.hoshino.cti.integration.jei;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import slimeknights.mantle.recipe.ingredient.EntityIngredient;

import java.util.ArrayList;
import java.util.List;

public class CursedDropRecipe {

    public final EntityIngredient entity;
    public final List<Drop> drops;

    public CursedDropRecipe(EntityIngredient entity, Drop... drops) {
        this.entity = entity;
        this.drops = new ArrayList<>();
        for (Drop drop : drops) {
            if (!drop.options().isEmpty()) {
                this.drops.add(drop);
            }
        }
    }

    public record Drop(List<ItemStack> options, int chance, int minAmount, int maxAmount, String note) {
    }

    public static List<CursedDropRecipe> createAll() {
        List<CursedDropRecipe> list = new ArrayList<>();
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.SHULKER),
                drop(el("astral_dust"), 20, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.SKELETON, EntityType.STRAY),
                drop(Items.ARROW, 100, 3, 15)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.ZOMBIE, EntityType.HUSK),
                drop(Items.SLIME_BALL, 25, 1, 3)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.SPIDER, EntityType.CAVE_SPIDER),
                drop(Items.STRING, 100, 2, 12)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.GUARDIAN),
                drop(Items.NAUTILUS_SHELL, 15, 1, 1),
                drop(Items.PRISMARINE_CRYSTALS, 100, 2, 5)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.ELDER_GUARDIAN),
                drop(Items.PRISMARINE_CRYSTALS, 100, 4, 16),
                drop(Items.PRISMARINE_SHARD, 100, 7, 28),
                oneOf("掉落时随机附魔(等级25~39)",
                        stack(Items.TRIDENT),
                        stack(el("guardian_heart")),
                        stack(Items.HEART_OF_THE_SEA),
                        stack(Items.ENCHANTED_GOLDEN_APPLE),
                        stack(Items.ENDER_EYE))));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.ENDERMAN),
                drop(Items.ENDER_EYE, 40, 1, 2)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.BLAZE),
                drop(Items.BLAZE_POWDER, 100, 0, 5)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.ZOMBIFIED_PIGLIN),
                drop(Items.GOLD_INGOT, 40, 1, 3),
                drop(Items.GLOWSTONE_DUST, 30, 1, 7)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.WITCH),
                drop(Items.GHAST_TEAR, 30, 1, 1),
                drop(Items.PHANTOM_MEMBRANE, 50, 1, 3)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.PILLAGER, EntityType.VINDICATOR),
                drop(Items.EMERALD, 100, 0, 4)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.VILLAGER),
                drop(Items.EMERALD, 100, 2, 6)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.CREEPER),
                drop(Items.GUNPOWDER, 100, 4, 12)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.PIGLIN_BRUTE),
                drop(Items.NETHERITE_SCRAP, 20, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.EVOKER),
                drop(Items.TOTEM_OF_UNDYING, 100, 1, 1),
                drop(Items.EMERALD, 100, 5, 20),
                drop(Items.ENCHANTED_GOLDEN_APPLE, 10, 1, 1),
                drop(Items.ENDER_PEARL, 30, 1, 3),
                drop(Items.BLAZE_ROD, 30, 2, 4),
                drop(Items.EXPERIENCE_BOTTLE, 50, 4, 10)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.WITHER_SKELETON),
                drop(Items.BLAZE_POWDER, 100, 0, 3),
                drop(Items.GHAST_TEAR, 20, 1, 1),
                drop(Items.NETHERITE_SCRAP, 7, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.GHAST),
                drop(Items.PHANTOM_MEMBRANE, 100, 1, 4)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.DROWNED),
                drop(Items.LAPIS_LAZULI, 30, 1, 3)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.VEX),
                drop(Items.GLOWSTONE_DUST, 100, 0, 2),
                drop(Items.PHANTOM_MEMBRANE, 30, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.PIGLIN),
                drop(Items.GOLD_INGOT, 50, 2, 4)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.RAVAGER),
                drop(Items.EMERALD, 100, 3, 10),
                drop(Items.LEATHER, 100, 2, 7),
                drop(Items.DIAMOND, 50, 0, 4)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.MAGMA_CUBE),
                drop(Items.BLAZE_POWDER, 50, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.CHICKEN),
                drop(Items.EGG, 50, 1, 1)));
        list.add(new CursedDropRecipe(EntityIngredient.of(EntityType.WITHER),
                drop(el("evil_essence"), 100, 1, 4)));
        return list;
    }

    public static Item el(String name) {
        return CommonUtil.itemFromId("enigmaticlegacy:" + name);
    }

    public static Drop drop(Item item, int chance, int min, int max) {
        if (item == null) {
            return new Drop(List.of(), chance, min, max, "");
        }
        return new Drop(List.of(new ItemStack(item)), chance, min, max, "");
    }

    public static Drop oneOf(String note, ItemStack... options) {
        List<ItemStack> list = new ArrayList<>();
        for (ItemStack option : options) {
            if (option != null && !option.isEmpty()) {
                list.add(option);
            }
        }
        return new Drop(list, 100, 1, 1, note);
    }

    public static ItemStack stack(Item item) {
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}
