package com.hoshino.cti.recipe;

import appeng.core.definitions.AEParts;
import appeng.items.parts.PartItem;
import com.google.gson.JsonObject;
import com.hoshino.cti.Cti;
import com.hoshino.cti.util.PanelCondition;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AnnihilationPanelRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final PanelCondition condition;
    public final PartItem<?> panel;
    public final int ticks;
    public final ResourceKey<Level> dimension;
    public final List<ChemicalStack<?>> chemicalOutputs;
    public final List<FluidStack> fluidOutputs;
    public final List<ItemStack> itemOutputs;
    public final Component additionalCondition;

    public static AnnihilationPanelRecipe EMPTY = new AnnihilationPanelRecipe(Cti.getResource("panel_recipe/empty"), AEParts.ANNIHILATION_PLANE.m_5456_(), PanelCondition.NULL,0,null,List.of(),List.of(),List.of());

    public AnnihilationPanelRecipe(ResourceLocation id, PartItem<?> panel, PanelCondition condition, int ticks, ResourceKey<Level> dimension, List<ChemicalStack<?>> chemicalOutputs, List<FluidStack> fluidOutputs, List<ItemStack> itemOutputs) {
        this.id = id;
        this.condition = condition;
        this.panel = panel;
        this.ticks = ticks;
        this.dimension = dimension;
        this.chemicalOutputs = chemicalOutputs;
        this.fluidOutputs = fluidOutputs;
        this.itemOutputs = itemOutputs;
        this.additionalCondition = null;
    }
    public AnnihilationPanelRecipe(ResourceLocation id, PartItem<?> panel, PanelCondition condition, int ticks, ResourceKey<Level> dimension, List<ChemicalStack<?>> chemicalOutputs, List<FluidStack> fluidOutputs, List<ItemStack> itemOutputs,Component component) {
        this.id = id;
        this.condition = condition;
        this.panel = panel;
        this.ticks = ticks;
        this.dimension = dimension;
        this.chemicalOutputs = chemicalOutputs;
        this.fluidOutputs = fluidOutputs;
        this.itemOutputs = itemOutputs;
        this.additionalCondition = component;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return pLevel.dimension()==this.dimension;
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.type;
    }

    public static class Type implements RecipeType<AnnihilationPanelRecipe>{
        public static Type type = new Type();
        public static final String ID = "panel_recipe";
    }
    public static class Serializer implements RecipeSerializer<AnnihilationPanelRecipe>{
        public static Serializer INSTANCE = new Serializer();
        @Override
        public AnnihilationPanelRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return EMPTY;
        }
        @Override
        public @Nullable AnnihilationPanelRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return EMPTY;
        }
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AnnihilationPanelRecipe pRecipe) {
        }
    }
}
