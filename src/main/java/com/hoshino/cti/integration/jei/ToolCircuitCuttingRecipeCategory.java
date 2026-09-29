package com.hoshino.cti.integration.jei;

import com.c2h6s.etshtinker.init.ItemReg.etshtinkerItems;
import com.hoshino.cti.Cti;
import com.hoshino.cti.recipe.PlateSmashRecipe;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.tools.TinkerTools;

import static com.hoshino.cti.Plugin.JEIPlugin.CIRCUIT_CUTTING;

public class ToolCircuitCuttingRecipeCategory extends ToolPlateSmashRecipeCategory{
    public ToolCircuitCuttingRecipeCategory(IGuiHelper helper) {
        super(helper, IModifiableDisplay.getDisplayStack(etshtinkerItems.constrained_plasma_saber.get()));
    }

    @Override
    public RecipeType<PlateSmashRecipe> getRecipeType() {
        return CIRCUIT_CUTTING;
    }
    public static final ResourceLocation UID = new ResourceLocation(Cti.MOD_ID,
            "tool_circuit_cut");

    @Override
    public Component getTitle() {
        return Component.literal("电路板切割");
    }
}
