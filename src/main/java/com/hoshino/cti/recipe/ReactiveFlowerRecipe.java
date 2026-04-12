package com.hoshino.cti.recipe;

import com.google.gson.JsonObject;
import com.hoshino.cti.Blocks.BlockEntity.botania.ReactiveFLowerBE;
import com.hoshino.cti.Cti;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReactiveFlowerRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final int elementalValue;
    @NotNull
    public final Item itemInput;
    public final ReactiveFLowerBE.ElementalType elementalType;
    public static final ReactiveFlowerRecipe EMPTY = new ReactiveFlowerRecipe(Cti.getResource("empty_reactive_flower"),0, Items.AIR, ReactiveFLowerBE.ElementalType.AERIAL);

    public ReactiveFlowerRecipe(ResourceLocation id, int elementalValue, @NotNull Item itemInput, ReactiveFLowerBE.ElementalType elementalType) {
        this.id = id;
        this.elementalValue = elementalValue;
        this.itemInput = itemInput;
        this.elementalType = elementalType;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
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
        return new Type();
    }

    public static class Type implements RecipeType<ReactiveFlowerRecipe>{
        public static ReactiveFlowerRecipe.Type type = new ReactiveFlowerRecipe.Type();
        public static final String ID = "reactive_flower";
    }
    public static class Serializer implements RecipeSerializer<ReactiveFlowerRecipe>{
        public static ReactiveFlowerRecipe.Serializer INSTANCE = new ReactiveFlowerRecipe.Serializer();
        @Override
        public ReactiveFlowerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return EMPTY;
        }
        @Override
        public @Nullable ReactiveFlowerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return EMPTY;
        }
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ReactiveFlowerRecipe pRecipe) {
        }
    }
}
