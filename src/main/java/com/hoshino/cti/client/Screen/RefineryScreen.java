package com.hoshino.cti.client.Screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.tconstruct.smeltery.client.screen.HeatingStructureScreen;
import slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu;

public class RefineryScreen extends HeatingStructureScreen {
    public RefineryScreen(HeatingStructureContainerMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }
}
