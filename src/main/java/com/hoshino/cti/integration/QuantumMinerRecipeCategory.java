package com.hoshino.cti.integration;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.QuantumMinerRecipe;
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

public class QuantumMinerRecipeCategory implements IRecipeCategory<QuantumMinerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "quantum_mining");

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/machine/quantum_miner_bg.png");


    private final IDrawable background;

    private final IDrawable icon;


    public QuantumMinerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 6, 37, 100, 26);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CtiItem.quantum_miner.get()));

    }

    @Override
    public RecipeType<QuantumMinerRecipe> getRecipeType() {
        return JEIPlugin.QUANTUM_MINING;
    }


    @Override
    public Component getTitle() {
        return Component.literal("量子开采");
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
    public void setRecipe(IRecipeLayoutBuilder builder, QuantumMinerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5).addItemStack(new ItemStack(CtiItem.compressed_singularity.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 5).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(QuantumMinerRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Component component = Component.literal("概率:").withStyle(ChatFormatting.WHITE).append(String.format("%.2f", recipe.getChance()));
        Minecraft.getInstance().font.draw(stack, component, recipeWidth / 2 - Minecraft.getInstance().font.width(component) - 20, (Minecraft.getInstance().font.lineHeight + 2) * 2 - 16, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
