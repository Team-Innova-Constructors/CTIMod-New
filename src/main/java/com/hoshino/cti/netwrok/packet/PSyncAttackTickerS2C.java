package com.hoshino.cti.netwrok.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PSyncAttackTickerS2C {
    public final int ticks;

    public PSyncAttackTickerS2C(int ticks) {
        this.ticks = ticks;
    }
    public PSyncAttackTickerS2C(FriendlyByteBuf buf){
        this.ticks = buf.readInt();
    }
    public void toByte(FriendlyByteBuf byteBuf){
        byteBuf.writeInt(this.ticks);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var player = SafeClientAccess.getPlayer();
        if (player != null) {
            player.attackStrengthTicker = ticks;
        }
    }
}
