package com.hoshino.cti.recipe;

import com.google.gson.JsonObject;
import com.hoshino.cti.Cti;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ReactorNeutronCollectorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final ItemStack catalyst;
    private final float consumption_rate;
    private final float efficiency;
    private final Ingredient ingredient = Ingredient.EMPTY;

    public ReactorNeutronCollectorRecipe(ResourceLocation id, ItemStack output, float efficiency, float consumption, ItemStack catalyst) {
        this.id = id;
        this.output = output;
        this.efficiency = efficiency;
        this.consumption_rate = consumption;
        this.catalyst = catalyst;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return !pLevel.isClientSide();
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getConsumptionRate() {
        return consumption_rate;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    public ItemStack getCatalyst() {
        return catalyst.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ReactorNeutronCollectorRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "reactor_neutron_collect";
    }

    // 别用json添加配方！
    @Deprecated
    public static class Serializer implements RecipeSerializer<ReactorNeutronCollectorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Cti.MOD_ID, "reactor_neutron_collect");

        @Override
        public ReactorNeutronCollectorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = new ItemStack(ModItems.neutron_pile.get());
            float efficiency = GsonHelper.getAsFloat(pSerializedRecipe, "efficiency");
            float consumption = GsonHelper.getAsFloat(pSerializedRecipe, "consumption_rate");
            ItemStack catalyst = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "catalyst"));
            return new ReactorNeutronCollectorRecipe(pRecipeId, output, efficiency, consumption, catalyst);
        }

        @Override
        public @Nullable ReactorNeutronCollectorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack output = new ItemStack(ModItems.neutron_pile.get());
            float efficiency = pBuffer.readFloat();
            float consumption_rate = (float) pBuffer.readDouble();
            ItemStack catalyst = pBuffer.readItem();
            return new ReactorNeutronCollectorRecipe(pRecipeId, output, efficiency, consumption_rate, catalyst);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ReactorNeutronCollectorRecipe pRecipe) {
            pBuffer.writeResourceLocation(pRecipe.id);
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeFloat(pRecipe.efficiency);
            pBuffer.writeDouble(pRecipe.consumption_rate);
            pBuffer.writeItemStack(pRecipe.getCatalyst(), false);
        }
    }
}
