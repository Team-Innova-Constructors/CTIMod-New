package com.hoshino.cti.Screen.menu;

import com.hoshino.cti.Cti;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ctiMenu {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Cti.MOD_ID);
    public static final RegistryObject<MenuType<AtmosphereExtractorMenu>> ATMOSPHERE_EXT_MENU = MENU_TYPE.register("atmosphere_extractor_menu", () -> IForgeMenuType.create(AtmosphereExtractorMenu::new));
    public static final RegistryObject<MenuType<AtmosphereCondensatorMenu>> ATMOSPHERE_CON_MENU = MENU_TYPE.register("atmosphere_condensator_menu", () -> IForgeMenuType.create(AtmosphereCondensatorMenu::new));
    public static final RegistryObject<MenuType<ReactorNeutronCollectorMenu>> NEUT_COL_MENU = MENU_TYPE.register("reactor_neutron_collector_menu", () -> IForgeMenuType.create(ReactorNeutronCollectorMenu::new));

    public static void register(IEventBus eventBus) {
        MENU_TYPE.register(eventBus);
    }
}
