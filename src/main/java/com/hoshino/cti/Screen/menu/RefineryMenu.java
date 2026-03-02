package com.hoshino.cti.Screen.menu;

import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import slimeknights.mantle.util.sync.ValidZeroDataSlot;
import slimeknights.tconstruct.shared.inventory.TriggeringMultiModuleContainerMenu;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.block.entity.module.MeltingModuleInventory;
import slimeknights.tconstruct.tables.menu.module.SideInventoryContainer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RefineryMenu extends TriggeringMultiModuleContainerMenu<HeatingStructureBlockEntity> {
    @Getter
    private final SideInventoryContainer<HeatingStructureBlockEntity> sideInventory;
    public RefineryMenu(int id, @Nullable Inventory inv, @Nullable HeatingStructureBlockEntity structure) {
        super(CtiMenu.REFINERY_MENU.get(), id, inv, structure);
        if (inv != null && structure != null) {
            MeltingModuleInventory inventory = structure.getMeltingInventory();
            sideInventory = new SideInventoryContainer<>(CtiMenu.REFINERY_MENU.get(), id, inv, structure, 0, 0, calcColumns(inventory.getSlots()));
            addSubContainer(sideInventory, true);

            Consumer<DataSlot> referenceConsumer = this::addDataSlot;
            ValidZeroDataSlot.trackIntArray(referenceConsumer, structure.getFuelModule());
            inventory.trackInts(array -> ValidZeroDataSlot.trackIntArray(referenceConsumer, array));
        } else {
            sideInventory = null;
        }
        addInventorySlots();
    }

    public RefineryMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileEntityFromBuf(buf, HeatingStructureBlockEntity.class));
    }

    public static int calcColumns(int slots) {
        return Math.min(4, (slots + 6) / 7);
    }
}