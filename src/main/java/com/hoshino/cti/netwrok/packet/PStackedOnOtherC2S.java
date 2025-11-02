package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PStackedOnOtherC2S {
    public final ItemStack stack;
    public final int slotIndex;
    public final boolean clickActionIndex;

    public PStackedOnOtherC2S(ItemStack stack, int slotIndex, boolean clickActionIndex) {
        this.stack = stack;
        this.slotIndex = slotIndex;
        this.clickActionIndex = clickActionIndex;
    }

    public PStackedOnOtherC2S(FriendlyByteBuf buf){
        this.stack = buf.readItem();
        this.slotIndex = buf.readInt();
        this.clickActionIndex = buf.readBoolean();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeInt(this.slotIndex);
        buf.writeBoolean(this.clickActionIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                if (player.containerMenu == player.inventoryMenu) SlotStackModifierHook.handleSlotStackOnOther(this.stack,player.inventoryMenu.getSlot(this.slotIndex),this.clickActionIndex? ClickAction.PRIMARY:ClickAction.SECONDARY,player);
            }
        });
        return true;
    }
}
