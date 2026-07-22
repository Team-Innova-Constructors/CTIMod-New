package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.util.CommonUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IDisplayModifierRecipe;
import slimeknights.tconstruct.plugin.jei.modifiers.ModifierRecipeCategory;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.item.CreativeSlotItem;
import slimeknights.tconstruct.tools.item.ModifierCrystalItem;

@Mixin(value = ModifierRecipeCategory.class,remap = false)
public class ModifierRecipeCategoryMixin {
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lslimeknights/tconstruct/library/recipe/modifiers/adding/IDisplayModifierRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V",at = @At("HEAD"))
    public void addCrystalOutput(IRecipeLayoutBuilder builder, IDisplayModifierRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(ModifierCrystalItem.withModifier(recipe.getModifier().getId()));
        CommonUtil.getResultSlotTypes(recipe.getDisplayResult()).forEach(slotType ->
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem.get(),1),slotType)));
    }
}
