package com.hoshino.cti.recipe;

import com.google.gson.JsonObject;
import com.hoshino.cti.Cti;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class QuantumMinerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final float chance;
    private final Ingredient ingredient = Ingredient.EMPTY;

    public QuantumMinerRecipe(ResourceLocation id, ItemStack output, float chance) {
        this.id = id;
        this.output = output;
        this.chance = chance;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return !pLevel.isClientSide();
    }

    public float getChance() {
        return chance;
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

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return QuantumMinerRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return QuantumMinerRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<QuantumMinerRecipe> {
        private Type() {
        }

        public static final QuantumMinerRecipe.Type INSTANCE = new QuantumMinerRecipe.Type();
        public static final String ID = "quantum_mining";
    }


    // 别用json添加配方！
    @Deprecated
    public static class Serializer implements RecipeSerializer<QuantumMinerRecipe> {
        public static final QuantumMinerRecipe.Serializer INSTANCE = new QuantumMinerRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Cti.MOD_ID, "quantum_mining");

        @Override
        public QuantumMinerRecipe fromJson(@NotNull ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));
            float chance = GsonHelper.getAsFloat(pSerializedRecipe, "chance");
            return new QuantumMinerRecipe(pRecipeId, output, chance);
        }

        // 从服务器中发送的数据中解码recipe，配方标识符不需要解码。
        @Override
        public @Nullable QuantumMinerRecipe fromNetwork(@NotNull ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack output = pBuffer.readItem();
            float chance = pBuffer.readFloat();
            return new QuantumMinerRecipe(pRecipeId, output, chance);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, QuantumMinerRecipe pRecipe) {
            pBuffer.writeResourceLocation(pRecipe.id);
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);
            pBuffer.writeFloat(pRecipe.chance);
        }
    }
}
