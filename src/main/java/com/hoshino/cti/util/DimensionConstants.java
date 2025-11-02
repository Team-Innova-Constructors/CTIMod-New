package com.hoshino.cti.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class DimensionConstants {
    public static final ResourceKey<Level> ULTRA_FLAT_DAY_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("kubejs", "ultra_flat_day"));
    public static final ResourceKey<Level> ULTRA_FLAT_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("kubejs", "ultra_flat"));
    public static final ResourceKey<Level> IONIZED_GLACIO = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "ionized_glacio"));
    public static final ResourceKey<Level> INFERNAL = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "infernal"));
    public static final ResourceKey<Level> JUPITER = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "jupiter"));
    public static final ResourceKey<Level> URANUS = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "uranus"));
    public static final ResourceKey<Level> VENUS = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "venus"));
    public static final ResourceKey<Level> MARS = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "mars"));
    public static final ResourceKey<Level> MERCURY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "mercury"));
    public static final ResourceKey<Level> GLACIO = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "glacio"));
    public static final ResourceKey<Level> MOON = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "moon"));

    public static final ResourceKey<Level> JUPITER_ORBIT = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ad_astra", "jupiter_orbit"));
}
