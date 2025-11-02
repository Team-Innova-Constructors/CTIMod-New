package com.hoshino.cti;

import com.hoshino.cti.Event.LivingEvents;
import com.hoshino.cti.Event.MobEffectEventHandler;
import com.hoshino.cti.Event.Sleep;
import com.hoshino.cti.Modifier.capability.*;
import com.hoshino.cti.Screen.menu.ctiMenu;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.register.*;
import com.hoshino.cti.util.BiomeUtil;
import com.hoshino.cti.util.tier.Roxy;
import com.hoshino.cti.world.feature.ctiConfiguredFeature;
import com.hoshino.cti.world.feature.ctiPlacedFeature;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

import java.util.List;

@Mod(Cti.MOD_ID)

public class Cti {
    public static boolean Mekenabled = ModList.get().isLoaded("mekanism");
    public static final String MOD_ID = "cti";

    public Cti() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CtiEntity.register(eventBus);
        CtiAttributes.ATTRIBUTES.register(eventBus);
        CtiItem.ITEMS.register(eventBus);
        CtiModifiers.MODIFIERS.register(eventBus);
        CtiFluid.FLUIDS.register(eventBus);
        CtiBlock.BLOCK.register(eventBus);
        CtiEffects.EFFECT.register(eventBus);
        CtiBlockEntityType.BLOCK_ENTITIES.register(eventBus);
        CtiInfusetype.INFUSE.register(eventBus);
        CtiItem.ASTRAITEM.init();
        CtiItem.VEHICLES.init();
        CtiEntityTickers.ENTITY_TICKERS.register(eventBus);
        CtiHostilityTrait.register();
        CtiEntity.ENTITY_TYPES.init();
        MinecraftForge.EVENT_BUS.register(new LivingEvents());
        MinecraftForge.EVENT_BUS.register(new MobEffectEventHandler());
        CtiPacketHandler.init();
        ctiMenu.MENU_TYPE.register(eventBus);
        CtiPotions.POTIONS.register(eventBus);
        CtiParticleType.PARTICLES.register(eventBus);
        CtiItem.registerPartModels();
        CtiSounds.sound.register(eventBus);
        //ctiRecipes.register(eventBus);
        ctiConfiguredFeature.CONFIGURED_FEATURES.register(eventBus);
        ctiPlacedFeature.PLACED_FEATURES.register(eventBus);
        if (Mekenabled) {
            CtiChemical.GAS.register(eventBus);
        }
        eventBus.addListener(this::commonSetup);

    }

    public static ResourceLocation getResource(String id) {
        return new ResourceLocation("cti", id);
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        //环境抗性cap，别忘了在这注册上
        ToolCapabilityProvider.register(ElectricShieldToolCap::new);
        ToolCapabilityProvider.register(ScorchShieldToolCap::new);
        ToolCapabilityProvider.register(FreezeShieldToolCap::new);
        ToolCapabilityProvider.register(PressureShieldToolCap::new);
        ToolCapabilityProvider.register(PressurizableToolCap::new);
        event.enqueueWork(BiomeUtil::init);
        event.enqueueWork(CtiRailgunProjectile::register);
        event.enqueueWork(CtiRitual::init);
        //机械动力土豆加农炮
        CtiPotatocannon.register();
        //powah反应堆冷却剂
        PowahSolid.init();
        CtiSlots.init();
        //药水配方
        CtiBrewing.init();
        TierSortingRegistry.registerTier(Roxy.instance, new ResourceLocation("cti:roxy"), List.of(Tiers.NETHERITE), List.of());
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }

    public static String makeDescriptionId(String type, String name) {
        return type + ".cti." + name;
    }
}
