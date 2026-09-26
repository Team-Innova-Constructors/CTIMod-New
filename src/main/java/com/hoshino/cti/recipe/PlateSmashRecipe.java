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
import org.jetbrains.annotations.Nullable;

public class PlateSmashRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    public final Item outputPlate;
    public final Item input;
    public static final PlateSmashRecipe EMPTY = new PlateSmashRecipe(Cti.getResource("empty_plate_smash"),Items.AIR, Items.AIR);

    public PlateSmashRecipe(ResourceLocation id, Item outputPlate, Item input) {
        this.id = id;
        this.outputPlate = outputPlate;
        this.input = input;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return new ItemStack(outputPlate.asItem(),9);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(outputPlate.asItem(),9);
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
        return Type.type;
    }

    public static class Type implements RecipeType<ReactiveFlowerRecipe>{
        public static PlateSmashRecipe.Type type = new PlateSmashRecipe.Type();
        public static final String ID = "tool_plate_smash";
    }
    public static class Serializer implements RecipeSerializer<PlateSmashRecipe>{
        public static Serializer INSTANCE = new Serializer();
        @Override
        public PlateSmashRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            return EMPTY;
        }
        @Override
        public @Nullable PlateSmashRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            return EMPTY;
        }
        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PlateSmashRecipe pRecipe) {
        }
    }
}
