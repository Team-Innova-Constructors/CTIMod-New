package com.hoshino.cti.recipe;

import com.hoshino.cti.Cti;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;


public class ctiRecipes {
    public static final SynchronizedDeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = SynchronizedDeferredRegister
            .create(ForgeRegistries.RECIPE_SERIALIZERS, Cti.MOD_ID);

    public static final RegistryObject<RecipeSerializer<EtchingRecipe>> ETCHING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS
            .register("modifier_etching",() -> LoadableRecipeSerializer.of(EtchingRecipe.LOADER));


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
