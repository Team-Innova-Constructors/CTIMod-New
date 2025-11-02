package com.hoshino.cti.recipe;

import com.google.gson.JsonObject;
import com.hoshino.cti.Cti;
import com.hoshino.cti.util.JsonUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class AtmosphereCondensorRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final FluidStack output;
    private final String BiomeString;
    private final Ingredient ingredient = Ingredient.EMPTY;

    public AtmosphereCondensorRecipe(ResourceLocation id, FluidStack output, String BiomeString) {
        this.id = id;
        this.output = output;
        this.BiomeString = BiomeString;
    }

    // 为了能够通过管理器获得配方，match必须返回true
    // 此方法用于管理容器是否输入有效。
    // 通过代用test检测
    // 检查容器内的物品和配方是否匹配。
    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return !pLevel.isClientSide();
    }

    public String getBiome() {
        return BiomeString;
    }

    public FluidStack getFluid() {
        return output;
    }

    // 构建配方
    // 返回了合成表的结果output
    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return ItemStack.EMPTY;
    }

    // 这个方法用于判断合成表是否可以在指定的dimensions合成。
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    // 获得合成表物品的copy()
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    //
    @Override
    public ResourceLocation getId() {
        return id;
    }

    // 返回Serializer 必须返回
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    // 返回type
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    // 注册新的合成的type
    public static class Type implements RecipeType<AtmosphereCondensorRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        // 标识了合成的类型，和json文件中的type一致
        public static final String ID = "atmosphere_condense";
    }

    // 别用json添加配方！
    @Deprecated
    public static class Serializer implements RecipeSerializer<AtmosphereCondensorRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Cti.MOD_ID, "atmosphere_condense");

        // 将JSON解码为recipe子类型
        @Override
        public AtmosphereCondensorRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            FluidStack output = JsonUtil.readFluid(pSerializedRecipe, "output");
            String biome = GsonHelper.getAsString(pSerializedRecipe, "biome");

            return new AtmosphereCondensorRecipe(pRecipeId, output, biome);
        }

        // 从服务器中发送的数据中解码recipe，配方标识符不需要解码。
        @Override
        public @Nullable AtmosphereCondensorRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            FluidStack output = pBuffer.readFluidStack();
            String biome = pBuffer.readUtf();
            return new AtmosphereCondensorRecipe(pRecipeId, output, biome);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, AtmosphereCondensorRecipe pRecipe) {
            pBuffer.writeResourceLocation(pRecipe.id);
            pBuffer.writeInt(pRecipe.getIngredients().size());
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeUtf(pRecipe.getBiome());
            pBuffer.writeFluidStack(pRecipe.getFluid());
        }
    }
}
