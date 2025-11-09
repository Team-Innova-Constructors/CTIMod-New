package com.hoshino.cti.util;

import com.hoshino.cti.Cti;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CtiTagkey {
    public static final TagKey<Item> OXYGEN_REGEN = TagKey.create(Registry.ITEM_REGISTRY, Cti.getResource("oxygen_provide"));
    public static final TagKey<Item> PRESSURE_MINOR = TagKey.create(Registry.ITEM_REGISTRY, Cti.getResource("pressure_protect_minor"));
    public static final TagKey<Item> ENVIRONMENT_ADV = TagKey.create(Registry.ITEM_REGISTRY, Cti.getResource("environment_protect_advanced"));
    public static final TagKey<Item> NDHGBLACKLIST = TagKey.create(Registry.ITEM_REGISTRY, Cti.getResource("ndhg_blacklist"));
    public static final TagKey<Block> ORE = TagKey.create(Registry.BLOCK_REGISTRY,new ResourceLocation("forge","ores"));
    public static final TagKey<Block> STONE = TagKey.create(Registry.BLOCK_REGISTRY,new ResourceLocation("forge","stone"));
}
