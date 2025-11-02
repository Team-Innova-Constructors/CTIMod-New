package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Modifier.aetherCompact.AmbrosiumPowered;
import com.hoshino.cti.library.modifier.hooks.SlotStackModifierHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

public class PAmbrosiumChargeC2S {
    public final int slotIndex;

    public PAmbrosiumChargeC2S(int slotIndex) {
        this.slotIndex = slotIndex;
    }
    public PAmbrosiumChargeC2S(FriendlyByteBuf buf){
        this.slotIndex = buf.readInt();
    }
    public void toByte(FriendlyByteBuf buf){
        buf.writeInt(this.slotIndex);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null&&player.containerMenu == player.inventoryMenu) {
                ItemStack stack = player.inventoryMenu.getSlot(this.slotIndex).getItem();
                ToolStack toolStack = ToolStack.from(stack);
                AmbrosiumPowered.chargeTool(toolStack);
            }
        });
        return true;
    }
}
