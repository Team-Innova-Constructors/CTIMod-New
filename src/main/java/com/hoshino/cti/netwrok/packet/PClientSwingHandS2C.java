package com.hoshino.cti.netwrok.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PClientSwingHandS2C {
    public final InteractionHand hand;

    public PClientSwingHandS2C(InteractionHand hand) {
        this.hand = hand;
    }
    public PClientSwingHandS2C(FriendlyByteBuf buf){
        this.hand = buf.readEnum(InteractionHand.class);
    }

    public void toByte(FriendlyByteBuf buf){
        buf.writeEnum(this.hand);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        var player = SafeClientAccess.getPlayer();
        if (player!=null){
            player.swing(hand);
        }
    }
}
