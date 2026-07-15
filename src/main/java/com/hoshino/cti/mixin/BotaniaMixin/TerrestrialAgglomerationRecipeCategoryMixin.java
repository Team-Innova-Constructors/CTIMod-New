package com.hoshino.cti.mixin.BotaniaMixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.client.integration.jei.TerrestrialAgglomerationRecipeCategory;

@Mixin(value = TerrestrialAgglomerationRecipeCategory.class,remap = false)
public class TerrestrialAgglomerationRecipeCategoryMixin {
    @Inject(method = "draw(Lvazkii/botania/api/recipe/TerrestrialAgglomerationRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lcom/mojang/blaze3d/vertex/PoseStack;DD)V",
    at = @At(value = "INVOKE", target = "Lvazkii/botania/client/gui/HUDHandler;renderManaBar(Lcom/mojang/blaze3d/vertex/PoseStack;IIIFII)V"))
    private void addExactManaDisplay(TerrestrialAgglomerationRecipe recipe, IRecipeSlotsView view, PoseStack ms, double mouseX, double mouseY, CallbackInfo ci){
        var str = "魔力消耗："+recipe.getMana();
        Minecraft.getInstance().font.draw(ms, str , 57 - (Minecraft.getInstance().font.width(str) / 2), 133, 0x000000);
    }

    @ModifyArg(method = "<init>",at = @At(value = "INVOKE", target = "Lmezz/jei/api/helpers/IGuiHelper;createBlankDrawable(II)Lmezz/jei/api/gui/drawable/IDrawableStatic;"),index = 1)
    private int modifyMaxY(int i){
        return 141;
    }
}
