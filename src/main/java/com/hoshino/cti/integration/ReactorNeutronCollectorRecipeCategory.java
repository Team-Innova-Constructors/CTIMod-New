package com.hoshino.cti.integration;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.ReactorNeutronCollectorRecipe;
import com.hoshino.cti.register.CtiItem;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.client.jei.ChemicalStackRenderer;
import mekanism.client.jei.MekanismJEI;
import mekanism.common.registries.MekanismGases;
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

public class ReactorNeutronCollectorRecipeCategory implements IRecipeCategory<ReactorNeutronCollectorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "reactor_neutron_collect");

    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/machine/neutron_collector_bg.png");


    private final IDrawable background;

    private final IDrawable icon;


    public ReactorNeutronCollectorRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 180, 59);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CtiItem.reactor_neutron_collector.get()));

    }

    @Override
    public RecipeType<ReactorNeutronCollectorRecipe> getRecipeType() {
        return JEIPlugin.NEUTRON_COLLECTING;
    }


    @Override
    public Component getTitle() {
        return Component.literal("过热钠中子素收集");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ReactorNeutronCollectorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 33).addIngredient(MekanismJEI.TYPE_GAS, new GasStack(MekanismGases.SUPERHEATED_SODIUM, 100000000)).setCustomRenderer(MekanismJEI.TYPE_GAS, new ChemicalStackRenderer<GasStack>(100000000, 16, 16));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 154, 33).addIngredient(MekanismJEI.TYPE_GAS, new GasStack(MekanismGases.SODIUM, 100000000)).setCustomRenderer(MekanismJEI.TYPE_GAS, new ChemicalStackRenderer<GasStack>(100000000, 16, 16));
        builder.addSlot(RecipeIngredientRole.INPUT, 82, 33).addItemStack(recipe.getCatalyst());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 10).addItemStack(recipe.getResultItem());
    }

    @Override
    public void draw(ReactorNeutronCollectorRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Component efficiency = Component.literal("基础效率: +").withStyle(ChatFormatting.LIGHT_PURPLE).append(String.format("%.2f", recipe.getEfficiency())).append("×");
        Component ConsumptionRate = Component.literal("催化剂消耗: ").withStyle(ChatFormatting.RED).append(String.format("%.2f", recipe.getConsumptionRate() * 1000)).append("‰");
        Minecraft.getInstance().font.draw(stack, efficiency, 8, 10, 0);
        Minecraft.getInstance().font.draw(stack, ConsumptionRate, 8, 18, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
