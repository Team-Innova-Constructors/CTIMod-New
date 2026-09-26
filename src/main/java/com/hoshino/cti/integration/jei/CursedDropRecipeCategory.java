package com.hoshino.cti.integration.jei;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.util.CommonUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import slimeknights.mantle.plugin.jei.MantleJEIConstants;
import slimeknights.mantle.plugin.jei.entity.EntityIngredientRenderer;

import java.util.ArrayList;
import java.util.List;

public class CursedDropRecipeCategory implements IRecipeCategory<CursedDropRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID, "cursed_drops");

    private final EntityIngredientRenderer entityRenderer = new EntityIngredientRenderer(32);
    private final IDrawable background;
    private final IDrawable icon;

    public CursedDropRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(122, 64);
        Item ring = CommonUtil.itemFromId("enigmaticlegacy:cursed_ring");
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ring != null ? ring : Items.COBWEB));
    }

    @Override
    public RecipeType<CursedDropRecipe> getRecipeType() {
        return JEIPlugin.CURSED_DROPS;
    }

    @Override
    public Component getTitle() {
        return Component.literal("七咒战利品");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CursedDropRecipe recipe, IFocusGroup focuses) {
        IIngredientAcceptor<?> entitySlot = builder.addSlot(RecipeIngredientRole.INPUT, 6, 16)
                .setCustomRenderer(MantleJEIConstants.ENTITY_TYPE, this.entityRenderer)
                .addIngredients(MantleJEIConstants.ENTITY_TYPE, recipe.entity.getDisplay());
        IIngredientAcceptor<?> eggs = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemStacks(recipe.entity.getEggs());
        builder.createFocusLink(entitySlot, eggs);

        for (int i = 0; i < recipe.drops.size(); i++) {
            CursedDropRecipe.Drop drop = recipe.drops.get(i);
            int x = 62 + (i % 3) * 18;
            int y = 14 + (i / 3) * 18;
            List<ItemStack> display = new ArrayList<>();
            for (ItemStack option : drop.options()) {
                var s = option.copy();
                s.setCount(Math.max(1, drop.minAmount()));
                display.add(s);
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStacks(display)
                    .addTooltipCallback((slotView, tooltip) -> {
                        if (drop.chance() >= 100) {
                            tooltip.add(Component.literal("必定掉落").withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(Component.literal(drop.chance() + "% 概率掉落").withStyle(ChatFormatting.GRAY));
                        }
                        if (drop.maxAmount() > drop.minAmount()) {
                            tooltip.add(Component.literal("数量: " + drop.minAmount() + "~" + drop.maxAmount()).withStyle(ChatFormatting.GRAY));
                        } else {
                            tooltip.add(Component.literal("数量: " + drop.minAmount()).withStyle(ChatFormatting.GRAY));
                        }
                        if (drop.options().size() > 1) {
                            tooltip.add(Component.literal("随机掉落其中之一").withStyle(ChatFormatting.GOLD));
                        }
                        if (!drop.note().isEmpty()) {
                            tooltip.add(Component.literal(drop.note()).withStyle(ChatFormatting.GOLD));
                        }
                    });
        }
    }

    @Override
    public void draw(CursedDropRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Minecraft.getInstance().font.draw(stack, "→", 44, 28, 0x3C3C3C);
        Minecraft.getInstance().font.draw(stack, "佩戴七咒之戒击杀时额外掉落", 2, 54, 0x3C3C3C);
    }
}
