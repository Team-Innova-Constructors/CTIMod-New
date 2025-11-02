package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Modifier.Developer.StellarBlade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PStellarBlade {


    public PStellarBlade() {

    }

    public PStellarBlade(FriendlyByteBuf buf) {
    }

    public void toByte(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        if (supplier.get().getSender() != null) {
            context.enqueueWork(() -> {
                ServerPlayer serverPlayer = supplier.get().getSender();
                StellarBlade.summonStars(serverPlayer);
            });
        }
        return true;
    }

}
