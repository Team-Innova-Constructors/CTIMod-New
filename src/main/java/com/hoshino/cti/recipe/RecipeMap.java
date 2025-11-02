package com.hoshino.cti.recipe;

import appeng.core.definitions.AEItems;
import appeng.core.definitions.AEParts;
import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.PanelCondition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class RecipeMap {
    public static ItemStack stack(ItemLike item, int count) {
        return new ItemStack(item, count);
    }

    public static ItemStack stack(ItemLike item) {
        return stack(item, 1);
    }

    //这个类用于真正添加配方
    //为了方便添加，物品和流体使用的是字符串

    //大气冷凝器
    //以第一个为例子  要填的值有4个
    //  "cryo_fuel1"                    -配方id
    //  "ad_astra:cryo_fuel"            -流体注册名
    //  25                              -流体数量
    //  "ad_astra:glacio_snowy_barrens" -群系
    public static final List<AtmosphereCondensorRecipe> CondensorRecipeList = new ArrayList<>(List.of(

            new AtmosphereCondensorRecipe(Cti.getResource("cryo_fuel1"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("ad_astra:cryo_fuel")), 25),
                    "ad_astra:glacio_snowy_barrens"),


            new AtmosphereCondensorRecipe(Cti.getResource("cryo_fuel2"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("ad_astra:cryo_fuel")), 25),
                    "ad_astra:glacio_ice_peaks"),
            new AtmosphereCondensorRecipe(Cti.getResource("overchargedneytronium"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("etshtinker:overchargedneutronium")), 1),
                    "cti:disordered_zone"),
            new AtmosphereCondensorRecipe(Cti.getResource("sufuric_acid"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:fuming_sulfuric_acid")), 500),
                    "ad_astra:infernal_venus_barrens"),
            new AtmosphereCondensorRecipe(Cti.getResource("sufuric_acid2"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:fuming_sulfuric_acid")), 500),
                    "ad_astra:venus_wastelands"),

            new AtmosphereCondensorRecipe(Cti.getResource("tritium"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:metalic_hellium")), 500),
                    "cti:jupiter"),
            new AtmosphereCondensorRecipe(Cti.getResource("deuterium"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:metalic_hydrogen")), 500),
                    "cti:uranus"),

            new AtmosphereCondensorRecipe(Cti.getResource("dense_lava"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:dense_lava")), 250),
                    "cti:infernal_molten_surface"),
            new AtmosphereCondensorRecipe(Cti.getResource("volatile_lava"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("kubejs:volatile_lava")), 250),
                    "cti:infernal"),
            new AtmosphereCondensorRecipe(Cti.getResource("crude_oil"),
                    new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("immersivepetroleum:crudeoil")), 1000),
                    "undergarden:smog_spires")

    ));

    //大气提取器
    //以第一个为例子  要填的值有4个
    //  "ice_shard"                     -配方id
    //  "ad_astra:ice_shard"            -物品注册名
    //  4                               -物品数量
    //  "ad_astra:glacio_snowy_barrens" -群系
    public static final List<AtmosphereExtractorRecipe> ExtractorRecipeList = new ArrayList<>(List.of(

            new AtmosphereExtractorRecipe(Cti.getResource("ice_shard"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:ice_shard")), 4),
                    "ad_astra:glacio_snowy_barrens"),


            new AtmosphereExtractorRecipe(Cti.getResource("ice_shard2"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:ice_shard")), 4),
                    "ad_astra:glacio_ice_peaks"),
            new AtmosphereExtractorRecipe(Cti.getResource("gas_hydrate"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:gas_hydrate")), 8),
                    "cti:uranus"),
            new AtmosphereExtractorRecipe(Cti.getResource("diamond_jupiter"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:diamond")), 4),
                    "cti:jupiter"),
            new AtmosphereExtractorRecipe(Cti.getResource("enriched_slag"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:enriched_slag")), 4),
                    "cti:infernal_molten_surface"),
            new AtmosphereExtractorRecipe(Cti.getResource("stable_slag"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:stable_slag")), 4),
                    "cti:infernal"),
            new AtmosphereExtractorRecipe(Cti.getResource("dryice"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("powah:dry_ice")), 64),
                    "ad_astra:martian_canyon_creek"),
            new AtmosphereExtractorRecipe(Cti.getResource("dryice2"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("powah:dry_ice")), 64),
                    "ad_astra:martian_polar_caps"),
            new AtmosphereExtractorRecipe(Cti.getResource("dryice2"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("powah:dry_ice")), 64),
                    "ad_astra:martian_wastelands")
    ));

    //量子采掘
    //以第一个为例子  要填的值有4个
    //  "copper_quantum"                -配方id
    //  "minecraft:copper_ore"          -物品注册名
    //  6                               -物品数量
    //  1.8f                            -产出概率（>1则增产）
    public static final List<QuantumMinerRecipe> MinerRecipeList = new ArrayList<>(List.of(

            new QuantumMinerRecipe(Cti.getResource("copper_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:copper_ore")), 18),
                    1.8f),


            new QuantumMinerRecipe(Cti.getResource("gold_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:gold_ore")), 5),
                    1.1f),
            new QuantumMinerRecipe(Cti.getResource("osmium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mekanism:osmium_ore")), 6),
                    1.21f),
            new QuantumMinerRecipe(Cti.getResource("iron_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:iron_ore")), 6),
                    1.25f),
            new QuantumMinerRecipe(Cti.getResource("coal_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:coal_ore")), 18),
                    1.5f),
            new QuantumMinerRecipe(Cti.getResource("diamond_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:diamond_ore")), 3),
                    1.25f),
            new QuantumMinerRecipe(Cti.getResource("glowstone_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:glowstone")), 3),
                    1.6f),
            new QuantumMinerRecipe(Cti.getResource("redstone_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:redstone_ore")), 6),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("lapis_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:lapis_ore")), 6),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("silver_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:silver_ore")), 5),
                    1.45f),
            new QuantumMinerRecipe(Cti.getResource("nickel_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:nickel_ore")), 6),
                    1.05f),

            new QuantumMinerRecipe(Cti.getResource("emerald_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:emerald_ore")), 6),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("tin_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:tin_ore")), 5),
                    1.45f),
            new QuantumMinerRecipe(Cti.getResource("lead_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:lead_ore")), 6),
                    1.05f),
            new QuantumMinerRecipe(Cti.getResource("aluminium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("immersiveengineering:ore_aluminum")), 6),
                    1.65f),

            new QuantumMinerRecipe(Cti.getResource("calorite_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:venus_calorite_ore")), 5),
                    0.55f),
            new QuantumMinerRecipe(Cti.getResource("desh_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_desh_ore")), 6),
                    0.75F),
            new QuantumMinerRecipe(Cti.getResource("ostrum_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:mars_ostrum_ore")), 6),
                    0.75F),

            new QuantumMinerRecipe(Cti.getResource("zirconium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tinkers_reforged:titanium_ore")), 3),
                    0.75f),
            new QuantumMinerRecipe(Cti.getResource("ttan_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tinkerscalibration:titanium_ore")), 4),
                    0.95F),


            new QuantumMinerRecipe(Cti.getResource("neutronium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:overdense_neutronium_ore")), 2),
                    0.2F),

            new QuantumMinerRecipe(Cti.getResource("bismuthinite_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:bismuthinite_ore_deepslate")), 3),
                    1.2f),
            new QuantumMinerRecipe(Cti.getResource("cobalt_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct:cobalt_ore")), 3),
                    1.35f),
            new QuantumMinerRecipe(Cti.getResource("debris_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:ancient_debris")), 3),
                    0.75f),
            new QuantumMinerRecipe(Cti.getResource("quartz_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:nether_quartz_ore")), 4),
                    1.3F),
            new QuantumMinerRecipe(Cti.getResource("zinc_ore_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:zinc_ore")), 5),
                    1.7F),
            new QuantumMinerRecipe(Cti.getResource("permafrost_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:permafrost")), 6),
                    0.5F),
            new QuantumMinerRecipe(Cti.getResource("ingot_uranium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mekanism:uranium_ore")), 8),
                    1.7F),
            new QuantumMinerRecipe(Cti.getResource("moon_cheese_ore_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:moon_cheese_ore")), 5),
                    0.75F)


    ));
    //高级量子采掘
    //和量子采掘一样，但是产出的是锭
    public static final List<QuantumMinerRecipe> AdvancedMinerRecipeList = new ArrayList<>(List.of(
            new QuantumMinerRecipe(Cti.getResource("copper_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:copper_ingot")), 36),
                    1.28f),
            new QuantumMinerRecipe(Cti.getResource("gold_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:gold_ingot")), 10),
                    1.1f),
            new QuantumMinerRecipe(Cti.getResource("osmium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mekanism:ingot_osmium")), 12),
                    1.21f),
            new QuantumMinerRecipe(Cti.getResource("iron_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:iron_ingot")), 36),
                    1.25f),
            new QuantumMinerRecipe(Cti.getResource("coal_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:coal")), 36),
                    1.5f),
            new QuantumMinerRecipe(Cti.getResource("diamond_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:diamond")), 6),
                    1.25f),
            new QuantumMinerRecipe(Cti.getResource("glowstone_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:glowstone_dust")), 24),
                    1.6f),
            new QuantumMinerRecipe(Cti.getResource("redstone_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:redstone")), 48),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("lapis_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:lapis_lazuli")), 48),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("silver_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:silver_ingot")), 10),
                    1.45f),
            new QuantumMinerRecipe(Cti.getResource("nickel_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:nickel_ingot")), 12),
                    1.05f),
            new QuantumMinerRecipe(Cti.getResource("emerald_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:emerald")), 12),
                    1.15f),
            new QuantumMinerRecipe(Cti.getResource("tin_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:tin_ingot")), 10),
                    1.45f),
            new QuantumMinerRecipe(Cti.getResource("lead_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermal:lead_ingot")), 12),
                    1.05f),
            new QuantumMinerRecipe(Cti.getResource("aluminium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("immersiveengineering:ingot_aluminum")), 12),
                    1.65f),
            new QuantumMinerRecipe(Cti.getResource("calorite_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:calorite_ingot")), 10),
                    0.55f),
            new QuantumMinerRecipe(Cti.getResource("desh_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:desh_ingot")), 12),
                    0.75F),
            new QuantumMinerRecipe(Cti.getResource("ostrum_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:ostrum_ingot")), 12),
                    0.75F),
            new QuantumMinerRecipe(Cti.getResource("zirconium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tinkers_reforged:titanium_ingot")), 6),
                    0.75f),
            new QuantumMinerRecipe(Cti.getResource("ttan_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tinkerscalibration:titanium_ingot")), 8),
                    0.95F),
            new QuantumMinerRecipe(Cti.getResource("neutronium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_ingot")), 2),
                    0.15F),
            new QuantumMinerRecipe(Cti.getResource("bismuthinite_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:bismuth_ingot")), 6),
                    1.2f),
            new QuantumMinerRecipe(Cti.getResource("cobalt_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct:cobalt_ingot")), 6),
                    1.35f),
            new QuantumMinerRecipe(Cti.getResource("debris_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:netherite_scrap")), 8),
                    0.75f),
            new QuantumMinerRecipe(Cti.getResource("quartz_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:quartz")), 8),
                    1.3F),
            new QuantumMinerRecipe(Cti.getResource("zinc_ore_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:zinc_ingot")), 10),
                    1.7F),
            new QuantumMinerRecipe(Cti.getResource("ingot_uranium_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("mekanism:ingot_uranium")), 16),
                    1.7F),
            new QuantumMinerRecipe(Cti.getResource("permafrost_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:permafrost")), 12),
                    0.5F),
            new QuantumMinerRecipe(Cti.getResource("moon_cheese_ore_quantum"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("ad_astra:cheese")), 10),
                    0.75F)


    ));

    //中子素收集
    //以第一个为例子  要填的值有6个
    //  "activated_chroma"                  -配方id
    //  "avaritia:neutron_nugget"           -输出物品（通常来说不用改，就是中子素粒，当然也能改成别的）
    //  1                                   -输出数量
    //  1.965f                              -基础效率倍率（每个催化剂单元增加一次效率，加算）
    //  0.000075f                           -基础催化剂消耗概率（每个催化剂单元增加一次，加算，一次只消耗一个，每tick计算）
    //  "etshtinker:activated_chroma_plate" -催化剂种类
    //  3                               -每个催化剂单元的催化剂数量
    public static final List<ReactorNeutronCollectorRecipe> NeutronRecipeList = new ArrayList<>(List.of(

            new ReactorNeutronCollectorRecipe(Cti.getResource("activated_chroma"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_nugget")), 1),
                    10.965f,
                    0.000025f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:activated_chroma_plate")), 3)),


            new ReactorNeutronCollectorRecipe(Cti.getResource("chroma"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_nugget")), 1),
                    12.875f,
                    0.00775f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:chroma_plate")), 5)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("ultra_dense"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_nugget")), 2),
                    22.675f,
                    0.00001f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:ultra_dense")), 3)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("protonium"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:electronium")), 1),
                    1.398f,
                    0.00956f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:protonium")), 3)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("electronium"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:protonium_nugget")), 1),
                    17.95f,
                    0.00156f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:electronium")), 1)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("violium_alloy"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_nugget")), 2),
                    32.198f,
                    0.00002f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:violium_alloy")), 2)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("atherium_alloy"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_nugget")), 4),
                    60.25f,
                    0.00002f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:atherium_alloy")), 1)),
            new ReactorNeutronCollectorRecipe(Cti.getResource("anti_neutronium"),
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("avaritia:neutron_ingot")), 1),
                    32f,
                    0.0000f,
                    new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("etshtinker:anti_neutronium")), 2))

    ));

    /**
     * 面板开采的配方，后续用于亚轨道空间采集面板。
     * 注：修改非亚轨道空间采集面板的配方没有任何意义，破坏面板和陨铁面板的配方只用于JEI显示。
     * panel 面板类型，填破坏面板一类的东西。。
     * condition 使用 util.PanelCondition 枚举类。包含在最高处向上、在最低处向下和无条件三种。
     * ticks 处理间隔。
     * dimension 维度，为null时允许任何维度。
     * 3个list分别是化学品、流体、物品的产出。产出类型的总和不要超过10，JEI显示不了。
     */
    public static final List<AnnihilationPanelRecipe> ANNIHILATION_PANEL_RECIPES = List.of(
            new AnnihilationPanelRecipe(Cti.getResource("sky_stone_dust"),
                    AEParts.ANNIHILATION_PLANE.m_5456_(),
                    PanelCondition.UPWARD,
                    200,
                    null,
                    List.of(),
                    List.of(),
                    List.of(AEItems.SKY_DUST.stack())
            ),
            new AnnihilationPanelRecipe(Cti.getResource("meteorium"),
                    CtiItem.meteorium_plane.get(),
                    PanelCondition.UPWARD,
                    2000,
                    null,
                    List.of(),
                    List.of(),
                    List.of(stack(CtiItem.meteorite_ore.get()))
            ),
            new AnnihilationPanelRecipe(Cti.getResource("aetheric_meteorite_ore"),
                    CtiItem.meteorium_plane.get(),
                    PanelCondition.UPWARD,
                    1000,
                    AetherDimensions.AETHER_LEVEL,
                    List.of(),
                    List.of(),
                    List.of(stack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("kubejs:aetheric_meteorite_ore")))),
                    Component.translatable("info.cti.panel_condition.night").withStyle(ChatFormatting.DARK_AQUA)
            )
    );
}
