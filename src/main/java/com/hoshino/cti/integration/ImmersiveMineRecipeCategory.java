package com.hoshino.cti.integration;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.StackWithChance;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.hoshino.cti.Plugin.JEIPlugin;
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
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.Iterator;

public class ImmersiveMineRecipeCategory implements IRecipeCategory<MineralMix> {
    // 区分合成分类的ID
    public static final ResourceLocation UID = new ResourceLocation(ImmersiveEngineering.MODID,
            "mineral_mix");
    // png file texture
    public static final ResourceLocation TEXTURE = new ResourceLocation(ImmersiveEngineering.MODID,
            "textures/gui/manual.png");

    // 合成分类的背景图片
    private final IDrawable background;
    // 合成分类的图标
    private final IDrawable icon;

    // 构造方法
    public ImmersiveMineRecipeCategory(IGuiHelper helper) {
        // 渲染背景图片。图片的开始位置和图片的结束的位置 u,v,width,height
        this.background = helper.createDrawable(TEXTURE, 20, 1, 146, 196);
        // 图标
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IEBlocks.Multiblocks.EXCAVATOR));

    }

    // 返回JEITutorialModPlugin定义的type
    @Override
    public RecipeType<MineralMix> getRecipeType() {
        return JEIPlugin.MINERAL_MIX;
    }

    // 合成界面的标题是什么
    @Override
    public Component getTitle() {
        return Component.literal("沉浸工程-矿脉");
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
    public void setRecipe(IRecipeLayoutBuilder builder, MineralMix recipe, IFocusGroup focuses) {
        Iterator<StackWithChance> iterator = Arrays.stream(recipe.outputs).iterator();
        int i = 0;
        while (iterator.hasNext()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 14, 32 + 20 * i).addItemStack(iterator.next().stack().get());
            i++;
        }
        int j = 0;
        Iterator<StackWithChance> iterator2 = Arrays.stream(recipe.spoils).iterator();
        while (iterator2.hasNext()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 14, 44 + 20 * i + 20 * j).addItemStack(iterator2.next().stack().get());
            j++;
        }
    }

    @Override
    public void draw(MineralMix recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Component component1 = Component.literal("矿脉名称：").append(Component.translatable(recipe.getTranslationKey()));
        Minecraft.getInstance().font.draw(stack, component1, 14, 12, 0);
        MutableComponent component2 = Component.literal("出现维度：");
        Iterator<ResourceKey<Level>> iterator1 = recipe.dimensions.stream().iterator();
        while (iterator1.hasNext()) {
            component2.append(Component.translatable("dimension." + iterator1.next().location().toLanguageKey()).append("  "));
        }
        Minecraft.getInstance().font.draw(stack, component2, 14, 20, 0);
        Iterator<StackWithChance> iterator = Arrays.stream(recipe.outputs).iterator();
        int i = 0;
        while (iterator.hasNext()) {
            Component component = Component.literal("比例 ").append(String.format("%.1f", iterator.next().chance() * 100)).append("%");
            Minecraft.getInstance().font.draw(stack, component, 34, 37 + 20 * i, 0);
            i++;
        }
        Minecraft.getInstance().font.draw(stack, Component.literal("附带："), 14, 32 + 20 * i, 0);
        int j = 0;
        Iterator<StackWithChance> iterator2 = Arrays.stream(recipe.spoils).iterator();
        while (iterator2.hasNext()) {
            Component component = Component.literal("比例 ").append(String.format("%.1f", iterator2.next().chance() * 100)).append("%");
            Minecraft.getInstance().font.draw(stack, component, 34, 49 + 20 * i + 20 * j, 0);
            j++;
        }
    }
}
