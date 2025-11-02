package com.hoshino.cti.content.registry;

import com.hoshino.cti.Cti;
import com.hoshino.cti.content.entityTicker.EntityTicker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class CtiRegistry {
    public static final ResourceKey<Registry<EntityTicker>> ENTITY_TICKER = ResourceKey.createRegistryKey(Cti.getResource("entity_ticker"));
    public static IForgeRegistry<EntityTicker> ENTITY_TICKER_REGISTRY;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event){
        RegistryBuilder<EntityTicker> builder = new RegistryBuilder<>();
        builder.setName(ENTITY_TICKER.location()).setDefaultKey(Cti.getResource("default"));
        event.create(builder,registry -> ENTITY_TICKER_REGISTRY = registry);
    }
}
