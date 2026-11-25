package com.hoshino.cti.netwrok.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PTurnPlayerBackS2C {
    public PTurnPlayerBackS2C() {
    }
    public PTurnPlayerBackS2C(FriendlyByteBuf buf){
    }
    public void toByte(FriendlyByteBuf byteBuf){
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var player = SafeClientAccess.getPlayer();
        if (player != null) {
            player.setXRot((90-player.getXRot())%180-90);
            player.setYRot((player.getYRot()+180)%360);
        }
    }
}
