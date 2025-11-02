package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PStackedOnMeC2S {
    public final ItemStack slotStack;
    public final ItemStack heldStack;
    public final int slotIndex;
    public final boolean clickActionIndex;

    public PStackedOnMeC2S(ItemStack slotStack, ItemStack heldStack, int slotIndex, boolean clickActionIndex) {
        this.slotStack = slotStack;
        this.heldStack = heldStack;
        this.slotIndex = slotIndex;
        this.clickActionIndex = clickActionIndex;
    }
    public PStackedOnMeC2S(FriendlyByteBuf buf){
        this.slotStack = buf.readItem();
        this.heldStack = buf.readItem();
        this.slotIndex = buf.readInt();
        this.clickActionIndex = buf.readBoolean();
    }
    public void toByte(FriendlyByteBuf buf) {
        buf.writeItem(this.slotStack);
        buf.writeItem(this.heldStack);
        buf.writeInt(this.slotIndex);
        buf.writeBoolean(this.clickActionIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SlotStackModifierHook.handleSlotStackOnMe(this.slotStack,this.heldStack,player.containerMenu.getSlot(this.slotIndex),this.clickActionIndex? ClickAction.PRIMARY:ClickAction.SECONDARY,player, SlotAccess.forContainer(player.getInventory(),this.slotIndex));
            }
        });
        return true;
    }
}
