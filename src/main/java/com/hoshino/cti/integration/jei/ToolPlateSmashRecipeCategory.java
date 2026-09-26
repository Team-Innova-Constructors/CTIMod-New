package com.hoshino.cti.integration.jei;

import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.PlateSmashRecipe;
import lombok.Getter;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.tools.TinkerTools;

import static com.hoshino.cti.Plugin.JEIPlugin.PLATE_SMASH;
import static slimeknights.tconstruct.plugin.jei.entity.SeveringCategory.BACKGROUND_LOC;

public class ToolPlateSmashRecipeCategory implements IRecipeCategory<PlateSmashRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "tool_plate_smash");
    @Override
    public RecipeType<PlateSmashRecipe> getRecipeType() {
        return PLATE_SMASH;
    }

    @Getter
    private final IDrawable background;
    @Getter
    private final IDrawable icon;
    public ToolPlateSmashRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND_LOC, 0, 78, 100, 38);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TinkerTools.sledgeHammer.get().getRenderTool());
    }

    @Override
    public Component getTitle() {
        return Component.literal("工具砸板");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlateSmashRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 76, 11).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 11).addItemStack(new ItemStack(recipe.input));
    }
}
