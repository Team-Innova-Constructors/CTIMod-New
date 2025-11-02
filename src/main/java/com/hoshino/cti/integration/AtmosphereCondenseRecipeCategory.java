package com.hoshino.cti.integration;

import com.hoshino.cti.Items.BiomeInfoItem;
import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.AtmosphereCondensorRecipe;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.BiomeUtil;
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
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;

public class AtmosphereCondenseRecipeCategory implements IRecipeCategory<AtmosphereCondensorRecipe> {
    // 区分合成分类的ID
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "atmosphere_condense");
    // png file texture
    public static final ResourceLocation TEXTURE = new ResourceLocation(Cti.MOD_ID,
            "textures/gui/machine/gui_condensor.png");

    // 合成分类的背景图片
    private final IDrawable background;
    // 合成分类的图标
    private final IDrawable icon;

    // 构造方法
    public AtmosphereCondenseRecipeCategory(IGuiHelper helper) {
        // 渲染背景图片。图片的开始位置和图片的结束的位置 u,v,width,height
        this.background = helper.createDrawable(TEXTURE, 42, 17, 100, 48);
        // 图标
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CtiItem.atmosphere_condensator.get()));

    }

    // 返回JEITutorialModPlugin定义的type
    @Override
    public RecipeType<AtmosphereCondensorRecipe> getRecipeType() {
        return JEIPlugin.ATMOSPHERE_CONDENSE;
    }

    // 合成界面的标题是什么
    @Override
    public Component getTitle() {
        return Component.literal("大气冷凝");
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

    // 添加合成表的输入slot和输出的slot
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AtmosphereCondensorRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 4).setFluidRenderer(40, false, 16, 40).addIngredient(ForgeTypes.FLUID_STACK, recipe.getFluid());
        if (BiomeUtil.INFO_LIST.contains(recipe.getBiome())){
            ItemStack stack = new ItemStack(CtiItem.BIOMES_ITEM.get());
            stack.getOrCreateTag().putString(BiomeInfoItem.KEY_BIOMES,recipe.getBiome());
            builder.addSlot(RecipeIngredientRole.INPUT,4,10).addItemStack(stack);
        }
    }

    @Override
    public void draw(AtmosphereCondensorRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        String biomes = recipe.getBiome();
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biomes));
        MutableComponent biomeC = Component.literal("群系:").withStyle(ChatFormatting.WHITE);
        biomeC.append(Component.translatable("biome." + key.location().toLanguageKey()).withStyle(ChatFormatting.LIGHT_PURPLE));
        if (!BiomeUtil.INFO_LIST.contains(recipe.getBiome())){
            Minecraft.getInstance().font.draw(stack, Component.literal("无可用信息"), 0, (Minecraft.getInstance().font.lineHeight + 2) * 2 - 14, 0);
        }
        Minecraft.getInstance().font.draw(stack, biomeC, 0, (Minecraft.getInstance().font.lineHeight + 2) * 2 - 20, 0);
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }
}
