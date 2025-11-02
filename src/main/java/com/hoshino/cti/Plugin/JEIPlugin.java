package com.hoshino.cti.Plugin;

import appeng.core.definitions.AEParts;
import blusunrize.immersiveengineering.api.crafting.IERecipeTypes;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.common.register.IEBlocks;
import com.c2h6s.etshtinker.init.ItemReg.etshtinkerItems;
import com.hoshino.cti.Items.BiomeInfoItem;
import com.hoshino.cti.Cti;
import com.hoshino.cti.integration.*;
import com.hoshino.cti.recipe.*;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.BiomeUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuel;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static RecipeType<AtmosphereExtractorRecipe> ATMOSPHERE_EXTRACT = new RecipeType<>(AtmosphereExtractRecipeCategory.UID, AtmosphereExtractorRecipe.class);
    public static RecipeType<AtmosphereCondensorRecipe> ATMOSPHERE_CONDENSE = new RecipeType<>(AtmosphereCondenseRecipeCategory.UID, AtmosphereCondensorRecipe.class);
    public static RecipeType<QuantumMinerRecipe> QUANTUM_MINING = new RecipeType<>(QuantumMinerRecipeCategory.UID, QuantumMinerRecipe.class);
    public static RecipeType<ReactorNeutronCollectorRecipe> NEUTRON_COLLECTING = new RecipeType<>(ReactorNeutronCollectorRecipeCategory.UID, ReactorNeutronCollectorRecipe.class);
    public static RecipeType<MineralMix> MINERAL_MIX = new RecipeType<>(ImmersiveMineRecipeCategory.UID, MineralMix.class);
    public static RecipeType<MeltingFuel> MELTING_FUEL = new RecipeType<>(MeltingFuelRecipeCategory.UID, MeltingFuel.class);
    public static RecipeType<AnnihilationPanelRecipe> PANEL_RECIPE = new RecipeType<>(AnnihilationPanelRecipeCategory.UID, AnnihilationPanelRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Cti.MOD_ID, "jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AtmosphereExtractRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AtmosphereCondenseRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new QuantumMinerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ReactorNeutronCollectorRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ImmersiveMineRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new MeltingFuelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new AnnihilationPanelRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<AtmosphereExtractorRecipe> recipesExtract = List.copyOf(RecipeMap.ExtractorRecipeList);
        List<AtmosphereCondensorRecipe> recipesCondense = List.copyOf(RecipeMap.CondensorRecipeList);
        List<QuantumMinerRecipe> quantumMining = List.copyOf(RecipeMap.MinerRecipeList);
        List<ReactorNeutronCollectorRecipe> neutronCollecting = List.copyOf(RecipeMap.NeutronRecipeList);
        List<AnnihilationPanelRecipe> panelRecipes = List.copyOf(RecipeMap.ANNIHILATION_PANEL_RECIPES);

        registration.addRecipes(ATMOSPHERE_EXTRACT, recipesExtract);
        registration.addRecipes(ATMOSPHERE_CONDENSE, recipesCondense);
        registration.addRecipes(QUANTUM_MINING, quantumMining);
        registration.addRecipes(NEUTRON_COLLECTING, neutronCollecting);
        registration.addRecipes(PANEL_RECIPE,panelRecipes);

        if (Minecraft.getInstance().level != null) {
            List<MineralMix> mineralMixes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IERecipeTypes.MINERAL_MIX.get());
            List<MeltingFuel> meltingFuels = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TinkerRecipeTypes.FUEL.get());

            registration.addRecipes(MINERAL_MIX, mineralMixes);
            registration.addRecipes(MELTING_FUEL, meltingFuels);
        }

        BiomeUtil.INFO_LIST.forEach(string -> {
            ResourceLocation location = new ResourceLocation(string);
            ItemStack stack = new ItemStack(CtiItem.BIOMES_ITEM.get());
            stack.getOrCreateTag().putString(BiomeInfoItem.KEY_BIOMES,string);
            registration.addIngredientInfo(stack, VanillaTypes.ITEM_STACK, Component.translatable("info.biomes."+location.toLanguageKey()));
        });
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CtiItem.atmosphere_extractor.get()), ATMOSPHERE_EXTRACT);
        registration.addRecipeCatalyst(new ItemStack(CtiItem.atmosphere_condensator.get()), ATMOSPHERE_CONDENSE);
        registration.addRecipeCatalyst(new ItemStack(CtiItem.quantum_miner.get()), QUANTUM_MINING);
        registration.addRecipeCatalyst(new ItemStack(CtiItem.quantum_miner_advanced.get()), QUANTUM_MINING);
        registration.addRecipeCatalyst(new ItemStack(CtiItem.reactor_neutron_collector.get()), NEUTRON_COLLECTING);
        registration.addRecipeCatalyst(new ItemStack(IEBlocks.Multiblocks.EXCAVATOR), MINERAL_MIX);

        registration.addRecipeCatalyst(new ItemStack(TinkerSmeltery.smelteryController), MELTING_FUEL);
        registration.addRecipeCatalyst(new ItemStack(TinkerSmeltery.scorchedAlloyer), MELTING_FUEL);
        registration.addRecipeCatalyst(new ItemStack(TinkerSmeltery.searedMelter), MELTING_FUEL);
        registration.addRecipeCatalyst(new ItemStack(TinkerSmeltery.foundryController), MELTING_FUEL);
        registration.addRecipeCatalyst(new ItemStack(etshtinkerItems.constrained_plasma_saber.get()), MELTING_FUEL);
        registration.addRecipeCatalyst(new ItemStack(CtiItem.meteorium_plane.get()), PANEL_RECIPE);
        registration.addRecipeCatalyst(new ItemStack(AEParts.ANNIHILATION_PLANE.m_5456_()), PANEL_RECIPE);
    }


}
