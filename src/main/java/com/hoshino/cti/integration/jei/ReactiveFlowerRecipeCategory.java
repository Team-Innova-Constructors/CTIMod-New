package com.hoshino.cti.integration.jei;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.recipe.QuantumMinerRecipe;
import com.hoshino.cti.recipe.ReactiveFlowerRecipe;
import com.hoshino.cti.register.CtiItem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.world.item.ItemStack;

import static mezz.jei.library.ingredients.IngredientInfoRecipe.recipeWidth;

public class ReactiveFlowerRecipeCategory implements IRecipeCategory<ReactiveFlowerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "reactive_flower");

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/jei/reactive_flower_bg.png");


    private final IDrawable background;

    private final IDrawable icon;


    public ReactiveFlowerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 84, 24);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CtiItem.REACTIVE_FLOWER.get()));

    }

    @Override
    public RecipeType<ReactiveFlowerRecipe> getRecipeType() {
        return JEIPlugin.REACTIVE_FLOWER;
    }


    @Override
    public Component getTitle() {
        return Component.literal("元素魔力");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ReactiveFlowerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4).addItemStack(new ItemStack(recipe.itemInput));
    }

    @Override
    public void draw(ReactiveFlowerRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        var elementalC = switch (recipe.elementalType){
            case EARTH -> Component.translatable("info.cti.elemental.earth").withStyle(style -> style.withColor(0x260000));
            case BLAZE -> Component.translatable("info.cti.elemental.blaze").withStyle(style -> style.withColor(0x87574A));
            case AERIAL -> Component.translatable("info.cti.elemental.aerial").withStyle(style -> style.withColor(0x87874A));
            case ICE -> Component.translatable("info.cti.elemental.ice").withStyle(style -> style.withColor(0x4F5E87));
        };
        Component component = Component.literal("元素:").withStyle(ChatFormatting.DARK_GRAY).append(elementalC);
        Minecraft.getInstance().font.draw(stack, component, 30, 4, 0);
        var valueC = Component.literal("值:").withStyle(ChatFormatting.DARK_GRAY).append(String.valueOf(recipe.elementalValue));
        Minecraft.getInstance().font.draw(stack, valueC, 30, 12, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
