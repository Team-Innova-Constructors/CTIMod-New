package com.hoshino.cti.util;

import com.hoshino.cti.Cti;
import earth.terrarium.ad_astra.AdAstra;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BiomeUtil {
    public static final ResourceKey<Biome> VENUS_BARRENS = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(AdAstra.MOD_ID, "infernal_venus_barrens"));
    public static final ResourceKey<Biome> VENUS_WASTELAND = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(AdAstra.MOD_ID, "venus_wastelands"));
    public static final ResourceKey<Biome> GLACIO_BARRENS = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(AdAstra.MOD_ID, "glacio_snowy_barrens"));
    public static final ResourceKey<Biome> GLACIO_ICE = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(AdAstra.MOD_ID, "glacio_ice_peaks"));

    public static final ResourceKey<Biome> IONIZED_MARE = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "ionized_mare"));
    public static final ResourceKey<Biome> IONIZED_GLACIO = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "ionized_glacio"));
    public static final ResourceKey<Biome> DISORDERED_ZONE = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "disordered_zone"));
    public static final ResourceKey<Biome> INFERNAL = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "infernal"));
    public static final ResourceKey<Biome> INFERNAL_MOLTEN = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "infernal_molten_surface"));
    public static final ResourceKey<Biome> JUPITER = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "jupiter"));
    public static final ResourceKey<Biome> URANUS = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Cti.MOD_ID, "uranus"));

    public static final List<String> INFO_LIST = List.of(
            "cti:ionized_mare",
            "cti:ionized_glacio",
            "cti:disordered_zone",
            "cti:infernal",
            "cti:infernal_molten_surface",
            "cti:jupiter",
            "cti:uranus",
            "undergarden:smog_spires"
    );


    public static void init() {

        IONIZE_LEVEL.put(IONIZED_MARE, 2.1f);
        IONIZE_LEVEL.put(IONIZED_GLACIO, 0.8f);
        IONIZE_LEVEL.put(DISORDERED_ZONE, 3.5f);

        SCORCH_LEVEL.put(INFERNAL_MOLTEN, 3.5f);
        SCORCH_LEVEL.put(INFERNAL, 2.1f);
        SCORCH_LEVEL.put(JUPITER, 3.8f);

        FREEZE_LEVEL.put(IONIZED_MARE, 2.2f);
        FREEZE_LEVEL.put(IONIZED_GLACIO, 3.9f);
        FREEZE_LEVEL.put(DISORDERED_ZONE, 1.1f);
        FREEZE_LEVEL.put(URANUS, 1.9f);

        PRESSURE_LEVEL.put(JUPITER, 3.25f);
        PRESSURE_LEVEL.put(URANUS, 1.80f);

    }

    public static String BiomekeyToString(ResourceKey<Biome> key) {
        return key != null ? "biome." + key.location().toLanguageKey() : "cti.gui.biome.null";
    }

    public static final Map<ResourceKey<Biome>, Float> IONIZE_LEVEL = new HashMap<>();
    public static final Map<ResourceKey<Biome>, Float> SCORCH_LEVEL = new HashMap<>();
    public static final Map<ResourceKey<Biome>, Float> FREEZE_LEVEL = new HashMap<>();
    public static final Map<ResourceKey<Biome>, Float> PRESSURE_LEVEL = new HashMap<>();


    public static float getBiomeIonizeLevel(Holder<Biome> holder) {
        ResourceKey<Biome> key = getBiomeKey(holder);
        return key != null ? IONIZE_LEVEL.getOrDefault(key, 0f) : 0f;
    }

    public static float getBiomeScorchLevel(Holder<Biome> holder) {
        ResourceKey<Biome> key = getBiomeKey(holder);
        return key != null ? SCORCH_LEVEL.getOrDefault(key, 0f) : 0f;
    }

    public static float getBiomeFreezeLevel(Holder<Biome> holder) {
        ResourceKey<Biome> key = getBiomeKey(holder);
        return key != null ? FREEZE_LEVEL.getOrDefault(key, 0f) : 0f;
    }

    public static float getBiomePressureLevel(Holder<Biome> holder) {
        ResourceKey<Biome> key = getBiomeKey(holder);
        return key != null ? PRESSURE_LEVEL.getOrDefault(key, 0f) : 0f;
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeKey(Holder<Biome> holder) {
        ResourceKey<Biome> key = null;
        if (holder.unwrapKey().isPresent()) {
            key = holder.unwrapKey().get();
        }
        return key;
    }

}
