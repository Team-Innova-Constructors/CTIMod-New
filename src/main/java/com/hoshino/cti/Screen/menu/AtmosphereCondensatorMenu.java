package com.hoshino.cti.Screen.menu;

import com.hoshino.cti.Blocks.BlockEntity.AtmosphereCondensatorEntity;
import com.hoshino.cti.Blocks.BlockEntity.GeneralMachineEntity;
import com.hoshino.cti.register.CtiBlock;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class AtmosphereCondensatorMenu extends GeneralMachineMenu {
    public final AtmosphereCondensatorEntity entity;
    private final Level level;
    private final ContainerData data;

    protected AtmosphereCondensatorMenu(int id, Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, inventory.player.level.getBlockEntity(buf.readBlockPos()), new SimpleContainerData(4));
    }

    public AtmosphereCondensatorMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ctiMenu.ATMOSPHERE_CON_MENU.get(), id, (GeneralMachineEntity) entity);
        checkContainerSize(inventory, 1);
        this.level = inventory.player.level;
        this.entity = (AtmosphereCondensatorEntity) entity;
        this.data = data;

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 152, 8));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 152, 26));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 152, 44));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 152, 62));
        });

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot0) {
        /*
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slot0);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (slot0 >= 0 && slot0 < 4) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot0 >= 31 && slot0 < 40 && !this.moveItemStackTo(itemstack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }


            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }
        return itemstack;
         */
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, CtiBlock.atmosphere_condensator.get());
    }

    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + 9 * i + 9, j * 18 + 8, i * 18 + 84));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getProgressScale() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int barSize = 26;
        return maxProgress != 0 && progress != 0 ? barSize * progress / maxProgress : 0;
    }

    public int getEnergyBarScale() {
        float energyStored = this.getEnergy();
        float maxEnergyStored = entity.getMaxEnergy();
        int barSize = 60;
        return energyStored != 0 && maxEnergyStored != 0 ? Mth.clamp((int) (barSize * energyStored / maxEnergyStored), 0, 60) : 0;
    }

    public int getFluidBarScale() {
        float fluidStored = this.getFluidstack().getAmount();
        float maxFluidStored = entity.FLUID_TANK.getCapacity();
        int barSize = 40;
        return fluidStored != 0 && maxFluidStored != 0 ? Mth.clamp((int) (barSize * fluidStored / maxFluidStored), 0, 40) : 0;
    }

}
