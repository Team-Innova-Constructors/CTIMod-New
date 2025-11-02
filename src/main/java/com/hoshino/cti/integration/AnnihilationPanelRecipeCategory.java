package com.hoshino.cti.integration;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.AnnihilationPanelRecipe;
import com.hoshino.cti.register.CtiItem;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.client.jei.MekanismJEI;
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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class AnnihilationPanelRecipeCategory implements IRecipeCategory<AnnihilationPanelRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "panel_recipe");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/jei/annihilation_panel_bg.png");


    private final IDrawable background;

    private final IDrawable icon;

    public AnnihilationPanelRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 184, 42);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CtiItem.meteorium_plane.get()));
    }

    @Override
    public RecipeType<AnnihilationPanelRecipe> getRecipeType() {
        return JEIPlugin.PANEL_RECIPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("面板采集");
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
    public void setRecipe(IRecipeLayoutBuilder builder, AnnihilationPanelRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,11,13).addItemStack(new ItemStack(recipe.panel));
        ArrayList<ItemStack> items =new ArrayList<>(List.copyOf(recipe.itemOutputs));
        ArrayList<FluidStack> fluids =new ArrayList<>(List.copyOf(recipe.fluidOutputs));
        ArrayList<ChemicalStack<?>> chemicals =new ArrayList<>(List.copyOf(recipe.chemicalOutputs));
        int added = 0;
        for (ItemStack stack:items){
            int w;
            int h;
            if (added<5){
                w = 85 + 18 * added;
                h = 4;
            } else {
                w = 85 + 18 * (added - 5);
                h = 22;
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT,w,h).addItemStack(stack);
            added++;
            if (added>=10) break;
        }
        if (added<10){
            for (FluidStack stack:fluids){
                int w;
                int h;
                if (added<5){
                    w = 85 + 18 * added;
                    h = 4;
                } else {
                    w = 85 + 18 * (added - 5);
                    h = 22;
                }
                builder.addSlot(RecipeIngredientRole.OUTPUT,w,h).addIngredient(ForgeTypes.FLUID_STACK,stack);
                added++;
                if (added>=10) break;
            }
        }
        if (added<10){
            for (ChemicalStack<?> stack:chemicals){
                int w;
                int h;
                if (added<5){
                    w = 85 + 18 * added;
                    h = 4;
                } else {
                    w = 85 + 18 * (added - 5);
                    h = 22;
                }
                addChemicalSlot(RecipeIngredientRole.OUTPUT,w,h,builder,stack);
                added++;
                if (added>=10) break;
            }
        }
    }

    public static void addChemicalSlot(RecipeIngredientRole role,int u,int v,IRecipeLayoutBuilder builder,ChemicalStack<?> stack){
        if (stack instanceof GasStack gasStack){
            builder.addSlot(role,u,v).addIngredient(MekanismJEI.TYPE_GAS,gasStack);
        } else if (stack instanceof InfusionStack infusionStack){
            builder.addSlot(role,u,v).addIngredient(MekanismJEI.TYPE_INFUSION,infusionStack);
        } else if (stack instanceof SlurryStack slurryStack){
            builder.addSlot(role,u,v).addIngredient(MekanismJEI.TYPE_SLURRY,slurryStack);
        } else if (stack instanceof PigmentStack pigmentStack){
            builder.addSlot(role,u,v).addIngredient(MekanismJEI.TYPE_PIGMENT,pigmentStack);
        }
    }

    @Override
    public void draw(AnnihilationPanelRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipe.dimension!=null){
            String dim = recipe.dimension.location().toLanguageKey();
            MutableComponent dimension = Component.literal("维度:").withStyle(ChatFormatting.DARK_GRAY);
            dimension.append(Component.translatable("dimension." + dim).withStyle(ChatFormatting.DARK_GRAY));
            Minecraft.getInstance().font.draw(stack, dimension, 2, 0, 0);
        }
        if (recipe.additionalCondition!=null){
            Minecraft.getInstance().font.draw(stack, recipe.additionalCondition, 32, 8, 0);
        }
        if (!recipe.condition.description.isEmpty()){
            Minecraft.getInstance().font.draw(stack, Component.translatable(recipe.condition.description).withStyle(Style.EMPTY.withColor(0x3c314b)), 2, 32, 0);
        }
        Minecraft.getInstance().font.draw(stack, Component.literal(recipe.ticks+" Ticks"), 32, 16, 0);
    }
}
