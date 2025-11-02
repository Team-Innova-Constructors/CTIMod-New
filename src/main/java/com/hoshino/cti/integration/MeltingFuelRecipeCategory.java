package com.hoshino.cti.integration;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.Cti;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class MeltingFuelRecipeCategory implements IRecipeCategory<MeltingFuel> {
    public static final ResourceLocation UID = new ResourceLocation(TConstruct.MOD_ID,
            "melting_fuel");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/jei/gui_melting_fuel.png");


    private final IDrawable background;

    private final IDrawable icon;

    public MeltingFuelRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 140, 26);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TinkerSmeltery.smelteryController.get()));
    }

    // 返回JEITutorialModPlugin定义的type
    @Override
    public RecipeType<MeltingFuel> getRecipeType() {
        return JEIPlugin.MELTING_FUEL;
    }

    // 合成界面的标题是什么
    @Override
    public Component getTitle() {
        return Component.literal("冶炼炉燃料");
    }

    //
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MeltingFuel recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5).setFluidRenderer(1, false, 16, 16).addIngredient(ForgeTypes.FLUID_STACK, recipe.getInputs().get(0));
    }

    @Override
    public void draw(MeltingFuel recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Component tick = Component.literal("燃烧时间").append(": ").append(recipe.getDuration() + "ticks").withStyle(ChatFormatting.DARK_GRAY);
        Component temp = Component.literal("燃烧温度").append(": ").append(recipe.getTemperature() + "℃").withStyle(ChatFormatting.DARK_GRAY);
        Minecraft.getInstance().font.draw(stack, tick, 26, 4, 0);
        Minecraft.getInstance().font.draw(stack, temp, 26, 14, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
