package com.hoshino.cti.Items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BiomeInfoItem extends Item {
    public static final String KEY_BIOMES = "biomes";
    public BiomeInfoItem() {
        super(new Item.Properties());
    }

    @Override
    public @NotNull String getDescriptionId(ItemStack pStack) {
        String s = pStack.getOrCreateTag().getString(KEY_BIOMES);
        if (!s.isEmpty()){
            ResourceLocation location = ResourceLocation.tryParse(s);
            if (location!=null){
                return "biome."+location.toLanguageKey();
            }
        }
        return super.getDescriptionId(pStack);
    }
}
