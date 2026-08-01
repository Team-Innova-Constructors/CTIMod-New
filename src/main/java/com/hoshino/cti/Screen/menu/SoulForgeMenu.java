package com.hoshino.cti.Screen.menu;

import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulForgeControllerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import slimeknights.mantle.util.sync.ValidZeroDataSlot;
import slimeknights.tconstruct.shared.inventory.TriggeringMultiModuleContainerMenu;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;

import javax.annotation.Nullable;

/**
 * 熔魂炉菜单：不绑定 {@code SideInventoryContainer}，因此熔炼物品栏及其侧栏 UI 不再出现。
 * 仍同步 {@link slimeknights.tconstruct.smeltery.block.entity.module.FuelModule} 的 int 数组，
 * 以保留燃料条渲染。
 * <p>额外用一个 {@link DataSlot} 同步实体熔炼倍率，供 GUI 实时显示，
 * 无需为整方块广播（没人开 GUI 时不会有人看到该数值）。
 */
public class SoulForgeMenu extends TriggeringMultiModuleContainerMenu<HeatingStructureBlockEntity> {
    /** 客户端镜像：当前实体熔炼倍率。屏幕读取该 DataSlot 即可获得实时值。 */
    private final DataSlot multiplierSync = DataSlot.standalone();

    public SoulForgeMenu(int id, @Nullable Inventory inv, @Nullable HeatingStructureBlockEntity structure) {
        this(CtiMenu.SOUL_FORGE_MENU.get(), id, inv, structure);
    }

    protected SoulForgeMenu(MenuType<SoulForgeMenu> type, int id, @Nullable Inventory inv, @Nullable HeatingStructureBlockEntity structure) {
        super(type, id, inv, structure);
        if (inv != null && structure != null) {
            ValidZeroDataSlot.trackIntArray(this::addDataSlot, structure.getFuelModule());
            if (structure instanceof SoulForgeControllerBlockEntity soul) {
                // 将倍率映射到 DataSlot：get 时读服务端方块实体的当前值，
                // set 时只更新镜像（客户端侧用 standalone 即可）
                addDataSlot(new DataSlot() {
                    @Override
                    public int get() {
                        return soul.getEntityMeltingMultiplier();
                    }
                    @Override
                    public void set(int value) {
                        multiplierSync.set(value);
                    }
                });
            } else {
                addDataSlot(multiplierSync);
            }
        }
        addInventorySlots();
    }

    public SoulForgeMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileEntityFromBuf(buf, HeatingStructureBlockEntity.class));
    }

    /** 客户端读取此方法获得当前同步到的实体熔炼倍率。 */
    public int getEntityMeltingMultiplier() {
        return multiplierSync.get();
    }
}