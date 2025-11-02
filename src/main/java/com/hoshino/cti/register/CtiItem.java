package com.hoshino.cti.register;

import appeng.api.parts.PartModels;
import appeng.items.parts.PartItem;
import appeng.items.parts.PartModelsHelper;
import cofh.core.util.helpers.AugmentDataHelper;
import cofh.thermal.lib.common.ThermalFlags;
import cofh.thermal.lib.item.AugmentItem;
import com.c2h6s.etshtinker.Items.StoriedMaterial;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import com.hoshino.cti.Blocks.AEParts.MeteoriumAnnihilationPlanePart;
import com.hoshino.cti.Entity.vehicles.rocketTier5;
import com.hoshino.cti.Items.*;
import com.hoshino.cti.Items.MekUpgrades.AdvancedUpgrade;
import com.hoshino.cti.Items.Vehicle.RocketItemTier5;
import com.hoshino.cti.Items.Wine.Unknown_Wine;
import com.hoshino.cti.Items.ingots.uriel_ingot;
import com.hoshino.cti.Items.pncMinigunAmmo.ElectroniumAmmo;
import com.hoshino.cti.Items.pncMinigunAmmo.ProtoniumAmmo;
import com.hoshino.cti.Items.pncMinigunAmmo.UltraDenseAmmo;
import com.hoshino.cti.integration.ArsNouveau.MeteorShowerRitual;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import mekanism.api.Upgrade;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import umpaz.brewinandchewin.common.item.BoozeItem;

import java.util.List;

import static cofh.core.init.CoreFlags.getFlag;
import static cofh.lib.util.constants.NBTTags.*;
import static cofh.thermal.lib.common.ThermalFlags.FLAG_UPGRADE_AUGMENTS;
import static earth.terrarium.ad_astra.common.registry.ModItems.ITEM_GROUP;
import static umpaz.brewinandchewin.common.registry.BCItems.TANKARD;

public class CtiItem {
    public static String FLAG_MACHINE_AUGMENTS = "machine_augments";
    public static String FLAG_DYNAMO_AUGMENTS = "dynamo_augments";
    public static String FLAG_AREA_AUGMENTS = "area_augments";

    public CtiItem() {
    }

    public static Item.Properties tankard_drinkItem() {
        return (new Item.Properties()).craftRemainder(TANKARD.get()).stacksTo(16).tab(CtiTab.FOOD);
    }

    public static Item.Properties bottle_drinkItem() {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16).tab(CtiTab.FOOD);
    }

    public static void registerPartModels() {
        PartModels.registerModels(PartModelsHelper.createModels(MeteoriumAnnihilationPlanePart.class));
    }
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "cti");
    public static final ResourcefulRegistry<Item> ASTRAITEM = ResourcefulRegistries.create(Registry.ITEM, "cti");
    public static final ResourcefulRegistry<Item> VEHICLES = ResourcefulRegistries.create(ASTRAITEM);
    public static final RegistryObject<Item> test = ITEMS.register("test", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> ultradense_ammo = ITEMS.register("ultradense_ammo", UltraDenseAmmo::new);
    public static final RegistryObject<Item> protonium_ammo = ITEMS.register("protonium_ammo", ProtoniumAmmo::new);
    public static final RegistryObject<Item> star_dragon_ammo = ITEMS.register("star_dragon_ammo",() -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> magic_crystal = ITEMS.register("magic_crystal", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> enriched_mana = ITEMS.register("enriched_mana", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> uriel_ingot = ITEMS.register("uriel_ingot", () -> new uriel_ingot(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> elysia = ITEMS.register("elysia", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> jd_heart = ITEMS.register("jd_heart", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> stellar_manyullyn = ITEMS.register("stellar_manyullyn", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.etsh").withStyle(ChatFormatting.LIGHT_PURPLE))));
    public static final RegistryObject<Item> invert_hoshino = ITEMS.register("invert_hoshino", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.invert_hoshino").withStyle(ChatFormatting.DARK_PURPLE))));
    public static final RegistryObject<Item> roxy_ingot = ITEMS.register("roxy_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.roxy_ingot").withStyle(ChatFormatting.BLUE))));

    public static final RegistryObject<Item> electronium_ammo = ITEMS.register("electronium_ammo", ElectroniumAmmo::new);
    public static final RegistryEntry<RocketItemTier5<rocketTier5>> TIER_5_ROCKET = VEHICLES.register("tier_5_rocket", () -> new RocketItemTier5<>(CtiEntity.TIER_5_ROCKET.get(), 5, new Item.Properties().tab(ITEM_GROUP).stacksTo(1).fireResistant().tab(CtiTab.MIXC)));
    public static final RegistryObject<Item> astra_tablet_5 = ITEMS.register("astra_tablet_5", () -> new PlanetGuiItem(new Item.Properties().tab(ITEM_GROUP).stacksTo(1).fireResistant().tab(CtiTab.MIXC), 5));
    public static final RegistryObject<Item> compressed_singularity = ITEMS.register("compressed_singularity", () -> new compressedSingularityItem(new Item.Properties().tab(ITEM_GROUP).stacksTo(64).fireResistant().tab(CtiTab.MIXC)));
    public static final RegistryObject<Item> recipe_test_item = ITEMS.register("recipe_test_item", RecipeTestItem::new);
    public static final RegistryObject<Item> flat_tablet = ITEMS.register("flat_tablet", FlatWorldTabletItem::new);
    public static final RegistryObject<Item> flat_tablet_day = ITEMS.register("flat_tablet_day", FlatWorldDayTabletItem::new);
    public static final RegistryObject<Item> STRANGE_ICE_CRYSTAL = ITEMS.register("strange_ice_crystal", StrangeIceCrystalItem::new);
    public static final RegistryObject<FieryJavelinItem> FIERY_JAVELIN = ITEMS.register("fiery_javelin", FieryJavelinItem::new);
    public static final RegistryObject<Item> ORB_OF_CURSE = ITEMS.register("orb_of_curse", OrbOfCurse::new);

    public static final RegistryObject<BlockItem> unipolar_magnet = ITEMS.register("unipolar_magnet", () -> new BlockItem(CtiBlock.unipolar_magnet.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> unipolar_magnet_budding = ITEMS.register("unipolar_magnet_budding", () -> new BlockItem(CtiBlock.unipolar_magnet_budding.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> overdense_glacio_stone = ITEMS.register("overdense_glacio_stone", () -> new BlockItem(CtiBlock.overdense_glacio_stone.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> ultra_dense_hydride_ore = ITEMS.register("ultra_dense_hydride_ore", () -> new BlockItem(CtiBlock.ultra_dense_hydride_ore.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> fracture_silicon = ITEMS.register("fracture_silicon", () -> new BlockItem(CtiBlock.fracture_silicon.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> fracture_silicon_budding = ITEMS.register("fracture_silicon_budding", () -> new BlockItem(CtiBlock.fracture_silicon_budding.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> rasterite = ITEMS.register("rasterite", () -> new BlockItem(CtiBlock.rasterite.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> rasterite_budding = ITEMS.register("rasterite_budding", () -> new BlockItem(CtiBlock.rasterite_budding.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<BlockItem> meteorite_ore = ITEMS.register("meteorite_ore", () -> new BlockItem(CtiBlock.meteorite_ore.get(), new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<StoriedMaterial> xenoglarium_ingot = ITEMS.register("xenoglarium_ingot", () -> new StoriedMaterial(new Item.Properties().tab(CtiTab.MATERIALS), List.of(
            Component.translatable("cti.tooltip.item.xenoglarium_ingot").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.xenoglarium_ingot2").withStyle(ChatFormatting.DARK_AQUA),
            Component.translatable("cti.tooltip.item.xenoglarium_ingot3").withStyle(ChatFormatting.AQUA),
            Component.translatable("etshtinker.item.tooltip.special").withStyle(ChatFormatting.LIGHT_PURPLE),
            Component.translatable("etshtinker.item.tooltip.special2").withStyle(ChatFormatting.LIGHT_PURPLE)
    )));


    public static final RegistryObject<PartItem<MeteoriumAnnihilationPlanePart>> meteorium_plane = ITEMS.register("meteorium_plane", () -> new PartItem<>(new Item.Properties().tab(CtiTab.MIXC), MeteoriumAnnihilationPlanePart.class, MeteoriumAnnihilationPlanePart::new));

    public static final RegistryObject<RitualTablet> meteor_shower_tablet = ITEMS.register("meteor_shower_tablet", () -> {
        RitualTablet tablet = new RitualTablet(new Item.Properties().tab(CtiTab.MIXC));
        tablet.ritual = new MeteorShowerRitual();
        return tablet;
    });

    public static final RegistryObject<BlockItem> alloy_centrifuge = ITEMS.register("alloy_centrifuge", () -> new TooltipedBlockItem(CtiBlock.alloy_centrifuge_block.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.alloy_centrifuge").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.alloy_centrifuge2").withStyle(ChatFormatting.GOLD),
            Component.translatable("cti.tooltip.item.alloy_centrifuge3").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> atmosphere_extractor = ITEMS.register("atmosphere_extractor", () -> new TooltipedBlockItem(CtiBlock.atmosphere_extractor.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.atmosphere_extractor").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.fe_max").append(": 75 MFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_consumption").append(": 750 kFE/t").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> atmosphere_condensator = ITEMS.register("atmosphere_condensator", () -> new TooltipedBlockItem(CtiBlock.atmosphere_condensator.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.atmosphere_condensator").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.fe_max").append(": 75 MFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_consumption").append(": 750 kFE/t").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> quantum_miner = ITEMS.register("quantum_miner", () -> new TooltipedBlockItem(CtiBlock.quantum_miner.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.quantum_miner").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.fe_max").append(": 2.14 GFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_consumption").append(": 134 MFE/t").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> quantum_miner_advanced = ITEMS.register("quantum_miner_advanced", () -> new TooltipedBlockItem(CtiBlock.quantum_miner_advanced.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.quantum_miner").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.quantum_miner_advanced").withStyle(ChatFormatting.LIGHT_PURPLE),
            Component.translatable("cti.tooltip.item.fe_max").append(": 2.14 GFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_consumption").append(": 536 MFE/t").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> reactor_neutron_collector = ITEMS.register("reactor_neutron_collector", () -> new TooltipedBlockItem(CtiBlock.reactor_neutron_collector.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.reactor_neutron_collector").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.reactor_neutron_collector2").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.reactor_neutron_collector3").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.reactor_neutron_collector4").withStyle(ChatFormatting.YELLOW),
            Component.translatable("cti.tooltip.item.fe_max").append(": 500 MFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_generate").append(": 100 kFE/t").withStyle(ChatFormatting.RED)
    )));
    public static final RegistryObject<BlockItem> sodium_cooler = ITEMS.register("sodium_cooler", () -> new TooltipedBlockItem(CtiBlock.sodium_cooler_block.get(), new Item.Properties().tab(CtiTab.MACHINE), List.of(
            Component.translatable("cti.tooltip.item.sodium_cooler").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.sodium_cooler2").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.sodium_cooler3").withStyle(ChatFormatting.AQUA),
            Component.translatable("cti.tooltip.item.fe_max").append(": 2 GFE").withStyle(ChatFormatting.RED),
            Component.translatable("cti.tooltip.item.fe_generate").append(": 50 MFE/t").withStyle(ChatFormatting.RED)
    )));


    public static final RegistryObject<Item> advanced_speed_augment = ITEMS.register("advanced_speed_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_MACHINE)
            .mod(TAG_AUGMENT_MACHINE_POWER, 15F)
            .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.3F)
            .mod(TAG_AUGMENT_RF_STORAGE, 2.5F)
            .mod(TAG_AUGMENT_RF_XFER, 12.5F)
            .build()).setShowInGroups(getFlag(FLAG_MACHINE_AUGMENTS)));
    public static final RegistryObject<Item> advanced_catalyst_augment = ITEMS.register("advanced_catalyst_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_MACHINE)
            .mod(TAG_AUGMENT_MACHINE_CATALYST, 0.18F)
            .mod(TAG_AUGMENT_MACHINE_ENERGY, 2F)
            .build()).setShowInGroups(getFlag(FLAG_MACHINE_AUGMENTS)));

    public static final RegistryObject<Item> advanced_dyano_augment = ITEMS.register("advanced_dyano_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_DYNAMO)
            .mod(TAG_AUGMENT_FLUID_STORAGE, 4)
            .mod(TAG_AUGMENT_DYNAMO_THROTTLE, 1)
            .mod(TAG_AUGMENT_DYNAMO_POWER, 40.0F)
            .mod(TAG_AUGMENT_DYNAMO_ENERGY, 2.0F)
            .build()).setShowInGroups(getFlag(FLAG_DYNAMO_AUGMENTS)));
    public static final RegistryObject<Item> Secondary_dyano_augment = ITEMS.register("secondary_dyano_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_DYNAMO)
            .mod(TAG_AUGMENT_FLUID_STORAGE, 2)
            .mod(TAG_AUGMENT_DYNAMO_THROTTLE, 1)
            .mod(TAG_AUGMENT_DYNAMO_POWER, 25.0F)
            .mod(TAG_AUGMENT_DYNAMO_ENERGY, 1.5F)
            .build()).setShowInGroups(getFlag(FLAG_DYNAMO_AUGMENTS)));

    public static final RegistryObject<Item> advanced_range_augment = ITEMS.register("advanced_range_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_AREA_EFFECT)
            .mod(TAG_AUGMENT_RADIUS, 3.0F)
            .build()).setShowInGroups(getFlag(FLAG_AREA_AUGMENTS)));

    public static final RegistryObject<Item> advanced_output_augment = ITEMS.register("advanced_output_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_MACHINE)
            .mod(TAG_AUGMENT_MACHINE_PRIMARY, 0.4F)
            .mod(TAG_AUGMENT_MACHINE_SECONDARY, 0.4F)
            .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.75F)
            .build()).setShowInGroups(getFlag(FLAG_MACHINE_AUGMENTS)));
    public static final RegistryObject<Item> advanced_fluid_tank_augment = ITEMS.register("advanced_fluid_tank_augment", () -> (new AugmentItem((new Item.Properties()).tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_FLUID)
            .mod(TAG_AUGMENT_FLUID_STORAGE, 16.0F)
            .build())).setShowInGroups(ThermalFlags.getFlag(ThermalFlags.FLAG_STORAGE_AUGMENTS)));
    public static final RegistryObject<Item> UPGRADE_AUGMENTS_4 = ITEMS.register("upgrade_augment_4", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_UPGRADE)
            .mod(TAG_AUGMENT_BASE_MOD, 5)
            .build()).setShowInGroups(getFlag(FLAG_UPGRADE_AUGMENTS)));

    public static final RegistryObject<Item> UPGRADE_AUGMENTS_5 = ITEMS.register("upgrade_augment_5", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_UPGRADE)
            .mod(TAG_AUGMENT_BASE_MOD, 7)
            .build()).setShowInGroups(getFlag(FLAG_UPGRADE_AUGMENTS)));
    public static final RegistryObject<Item> UPGRADE_AUGMENTS_6 = ITEMS.register("upgrade_augment_6", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_UPGRADE)
            .mod(TAG_AUGMENT_BASE_MOD, 14)
            .build()).setShowInGroups(getFlag(FLAG_UPGRADE_AUGMENTS)));

    //黑雾级别
    //黑雾能源炉插件
    public static final RegistryObject<Item> extereme_dyano_augment = ITEMS.register("extereme_dyano_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_DYNAMO)
            .mod(TAG_AUGMENT_FLUID_STORAGE, 8)
            .mod(TAG_AUGMENT_DYNAMO_THROTTLE, 1)
            .mod(TAG_AUGMENT_DYNAMO_POWER, 100.0F)
            .mod(TAG_AUGMENT_DYNAMO_ENERGY, 3.0F)
            .build()).setShowInGroups(getFlag(FLAG_DYNAMO_AUGMENTS)));
    //黑雾速度插件
    public static final RegistryObject<Item> extereme_speed_augment = ITEMS.register("extereme_speed_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_MACHINE)
            .mod(TAG_AUGMENT_MACHINE_POWER, 40F)
            .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.1F)
            .mod(TAG_AUGMENT_RF_STORAGE, 10F)
            .mod(TAG_AUGMENT_RF_XFER, 40.0F)
            .build()).setShowInGroups(getFlag(FLAG_MACHINE_AUGMENTS)));
    //黑雾深度催化
    public static final RegistryObject<Item> extereme_output_augment = ITEMS.register("extereme_output_augment", () -> new ThermalAugment(new Item.Properties().tab(CtiTab.MIXC), AugmentDataHelper.builder()
            .type(TAG_AUGMENT_TYPE_MACHINE)
            .mod(TAG_AUGMENT_MACHINE_PRIMARY, 1F)
            .mod(TAG_AUGMENT_MACHINE_SECONDARY, 1F)
            .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.25F)
            .build()).setShowInGroups(getFlag(FLAG_MACHINE_AUGMENTS)));


    public static final RegistryObject<Item> upgrade_electronium = ITEMS.register("upgrade_electronium", () -> new AtmosphereUpgradeItem(3f, 1.75f));
    public static final RegistryObject<Item> upgrade_violium = ITEMS.register("upgrade_violium", () -> new AtmosphereUpgradeItem(4.5f, 0.5f));
    public static final RegistryObject<Item> upgrade_aetherium = ITEMS.register("upgrade_aetherium", () -> new AtmosphereUpgradeItem(10.5f, 2f));
    public static final RegistryObject<Item> test_tool = ITEMS.register("test_tool", () -> new TestTool(new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<Item> Vein_Remove_Tool = ITEMS.register("vein_remove_tool", () -> new VeinRemoveTool(new Item.Properties().tab(CtiTab.MIXC)));


    //材料
    public static final RegistryObject<Item> nitro_supersteel = ITEMS.register("nitro_supersteel", () -> new Item(new Item.Properties().tab(CtiTab.MATERIALS)));
    public static final RegistryObject<Item> hot_supersteel = ITEMS.register("hot_supersteel", () -> new Item(new Item.Properties().tab(CtiTab.MIXC)));
    public static final RegistryObject<Item> awakesaintchef_ingot = ITEMS.register("awakesaintchef_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.awakesaintchef_ingot.desc1").withStyle(ChatFormatting.DARK_PURPLE), Component.translatable("cti.tooltip.item.awakesaintchef_ingot.desc2").withStyle(ChatFormatting.DARK_PURPLE))));
    public static final RegistryObject<Item> omniscient_gold_ingot = ITEMS.register("omniscient_gold_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.omniscient_gold_ingot.desc1").withStyle(ChatFormatting.DARK_PURPLE), Component.translatable("cti.tooltip.item.omniscient_gold_ingot.desc2").withStyle(ChatFormatting.DARK_PURPLE))));
    public static final RegistryObject<Item> xinian_ingot = ITEMS.register("xinian_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.xinian_ingot.desc1").withStyle(ChatFormatting.DARK_PURPLE), Component.translatable("cti.tooltip.item.xinian_ingot.desc2").withStyle(ChatFormatting.DARK_PURPLE))));
    public static final RegistryObject<Item> bloodgod_ingot = ITEMS.register("bloodgod_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.bloodgod_ingot.desc1").withStyle(ChatFormatting.DARK_PURPLE), Component.translatable("cti.tooltip.item.bloodgod_ingot.desc2").withStyle(ChatFormatting.DARK_PURPLE))));
    public static final RegistryObject<Item> holographic_zero_ingot = ITEMS.register("holographic_zero_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS), List.of(Component.translatable("cti.tooltip.item.holographic_zero_ingot.desc1").withStyle(ChatFormatting.DARK_PURPLE), Component.translatable("cti.tooltip.item.holographic_zero_ingot.desc2").withStyle(ChatFormatting.DARK_PURPLE))));
    //弹射物物品
    public static final RegistryObject<Item> star_blaze = ITEMS.register("star_blaze", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> star_frozen = ITEMS.register("star_frozen", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> star_ionize = ITEMS.register("star_ionize", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> star_pressure = ITEMS.register("star_pressure", () -> new Item(new Item.Properties()));
    //吃的喝的
    public static final RegistryObject<Item> COLD_GOBBERWINE = ITEMS.register("cold_gobberwine", () -> new BoozeItem(1, 5, tankard_drinkItem().food(CtiWine.COLD_GOBBERWINE)));
    public static final RegistryObject<Item> ETHANOL_ABSOLUTE = ITEMS.register("ethanol_absolute", () -> new BoozeItem(3, 20, tankard_drinkItem().food(CtiWine.ETHANOL_ABSOLUTE)));
    public static final RegistryObject<Item> cornflower_beer = ITEMS.register("cornflower_beer", () -> new BoozeItem(1, 5, tankard_drinkItem().food(CtiWine.cornflower_beer)));
    public static final RegistryObject<Item> etbeer = ITEMS.register("etbeer", () -> new BoozeItem(3, 10, tankard_drinkItem().food(CtiWine.etbeer)));
    public static final RegistryObject<Item> xuerou_wine = ITEMS.register("xuerou_wine", () -> new BoozeItem(3, 5, tankard_drinkItem().food(CtiWine.xuerou_wine)));
    public static final RegistryObject<Item> mahoushaojiu_wine = ITEMS.register("mahoushaojiu_wine", () -> new BoozeItem(1, 10, tankard_drinkItem().food(CtiWine.mahoushaojiu_wine)));
    public static final RegistryObject<Item> UNKNOWN_WINE = ITEMS.register("unknown_wine", () -> new Unknown_Wine(tankard_drinkItem().food(CtiWine.UNKNOWN_WINE).craftRemainder(TANKARD.get())));
    public static final RegistryObject<Item> qdbeer = ITEMS.register("qdbeer", () -> new BoozeItem(1, 1, bottle_drinkItem().food(CtiWine.qdbeer)));
    public static final RegistryObject<Item> boomwine = ITEMS.register("boomwine", () -> new BoozeItem(1, 5, bottle_drinkItem().food(CtiWine.boomwine)));
    public static final RegistryObject<Item> fishbone_wine = ITEMS.register("fishbone_wine", () -> new BoozeItem(1, 1, tankard_drinkItem().food(CtiWine.fishbone_wine).craftRemainder(Items.ENCHANTED_GOLDEN_APPLE)));

    //佛糖
    public static final RegistryObject<Item> heng_sugar = ITEMS.register("heng_sugar", () -> new FoSugar(new Item.Properties().tab(CtiTab.MIXC),CtiEffects.heng.get()));
    public static final RegistryObject<Item> ha_sugar = ITEMS.register("ha_sugar", () -> new FoSugar(new Item.Properties().tab(CtiTab.MIXC),CtiEffects.ha.get()));
    public static final RegistryObject<Item> strong_sugar = ITEMS.register("strong_sugar", () -> new FoSugar(new Item.Properties().tab(CtiTab.MIXC),CtiEffects.strong.get()));
    public static final RegistryObject<Item> covert_sugar = ITEMS.register("covert_sugar", () -> new FoSugar(new Item.Properties().tab(CtiTab.MIXC),CtiEffects.covert.get()));
    public static final RegistryObject<Item> nakshatra_sugar = ITEMS.register("nakshatra_sugar", () -> new FoSugar(new Item.Properties().tab(CtiTab.MIXC),CtiEffects.nakshatra.get()));
    public static final RegistryObject<Item> soul_spell = ITEMS.register("soul_spell", () -> new SoulSpell(new Item.Properties().tab(CtiTab.MIXC).stacksTo(1)));

    //矿脉生成
    //ResourceLocation部分需要完整的MODID和path(同实际路径)
    public static final RegistryObject<Item> bi_mineral_generator = ITEMS.register("bi_mineral_generator", () -> new VeinGeneratorItem(new Item.Properties().tab(CtiTab.MIXC),new ResourceLocation("etshtinker","immersiveengineering/mineral/bismuthinite")));


    //mek高级升级
    public static final RegistryObject<Item> chroma_upgrade_speed_kit = ITEMS.register("chroma_upgrade_speed_kit", () -> new AdvancedUpgrade(10, true, List.of(Upgrade.SPEED)));
    public static final RegistryObject<Item> aetherium_upgrade_speed_kit = ITEMS.register("aetherium_upgrade_speed_kit", () -> new AdvancedUpgrade(14, true, List.of(Upgrade.SPEED)));
    //public static final RegistryObject<Item> anti_upgrade_speed_kit = ITEMS.register("anti_upgrade_speed_kit", () -> new AdvancedUpgrade(20,true,List.of(Upgrade.SPEED)));
    public static final RegistryObject<Item> upgrade_double_kit = ITEMS.register("upgrade_double_kit", () -> new AdvancedUpgrade(8, false, List.of(Upgrade.SPEED, Upgrade.ENERGY)));
    public static final RegistryObject<Item> upgrade_energy_kit = ITEMS.register("upgrade_energy_kit", () -> new AdvancedUpgrade(8, false, List.of(Upgrade.ENERGY)));
    public static final RegistryObject<Item> upgrade_gas_kit = ITEMS.register("upgrade_gas_kit", () -> new AdvancedUpgrade(8, false, List.of(Upgrade.GAS)));
    public static final RegistryObject<Item> upgrade_speed_kit = ITEMS.register("upgrade_speed_kit", () -> new AdvancedUpgrade(8, false, List.of(Upgrade.SPEED)));
    public static final RegistryObject<Item> upgrade_energy_kit_2 = ITEMS.register("upgrade_energy_kit_2", () -> new AdvancedUpgrade(16, false, List.of(Upgrade.ENERGY)));
    public static final RegistryObject<Item> upgrade_energy_kit_3 = ITEMS.register("upgrade_energy_kit_3", () -> new AdvancedUpgrade(32, false, List.of(Upgrade.ENERGY)));
    public static final RegistryObject<Item> upgrade_speed_kit_1 = ITEMS.register("upgrade_speed_kit_1", () -> new AdvancedUpgrade(12, false, List.of(Upgrade.SPEED)));
    public static final RegistryObject<Item> upgrade_speed_kit_2 = ITEMS.register("upgrade_speed_kit_2", () -> new AdvancedUpgrade(16, false, List.of(Upgrade.SPEED)));

    public static final RegistryObject<BlockItem> aluminium_glass = ITEMS.register("aluminium_glass", () -> new BlockItem(CtiBlock.aluminium_glass.get(), new Item.Properties().tab(CtiTab.MIXC)){
        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
            pTooltip.add(Component.literal("高强度的铝玻璃,他会弱化周围生物的攻击,半径4格内每多一块就会降低1%伤害,至多降低40%").withStyle(style -> style.withColor(0xffaaff)));
            pTooltip.add(Component.literal("对于穿甲/穿魔伤害无效！").withStyle(style -> style.withColor(0xff557f)));
        }
    });
    public static final RegistryObject<Item> star_dragon_ingot = ITEMS.register("star_dragon_ingot", () -> new TooltipedItem(new Item.Properties().tab(CtiTab.MATERIALS),List.of(
            Component.literal("游荡于星界之龙").withStyle(style -> style.withColor(0x5500ff))
    )));

    public static final RegistryObject<Item> BIOMES_ITEM = ITEMS.register("biomes_item",BiomeInfoItem::new);

    /*
    //群系JEI物品
    public static class BiomesItems{
        private static final DeferredRegister<Item> BIOMES_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, cti.MOD_ID);
        public static void init(IEventBus eventBus){
            ForgeRegistries.BIOMES.getKeys().forEach(location -> {
                BIOMES_ITEMS.register()
            });
        }
    }

     */

}
