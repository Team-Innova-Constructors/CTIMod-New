package com.hoshino.cti.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class SearchTools {
    public static Item findItem(String registryNameString) {
        if (registryNameString == null || !registryNameString.contains(":")) {
            return Items.AIR;
        }
        ResourceLocation location = new ResourceLocation(registryNameString);
        Item result = ForgeRegistries.ITEMS.getValue(location);
        if (result == null) {
            return Items.AIR;
        }
        return result;
    }
    public static Item[] findItemList(String[] itemList) {
        if (itemList == null || itemList.length == 0) {
            return new Item[0];
        }
        return Arrays.stream(itemList)
                .map(SearchTools::getItemFromRegistry)
                .toArray(Item[]::new);
    }
    private static Item getItemFromRegistry(String registryNameString) {
        if (registryNameString == null || !registryNameString.contains(":")) {
            return Items.AIR;
        }
        ResourceLocation location = new ResourceLocation(registryNameString);
        Item result = ForgeRegistries.ITEMS.getValue(location);
        return (result != null) ? result : Items.AIR;
    }
}
