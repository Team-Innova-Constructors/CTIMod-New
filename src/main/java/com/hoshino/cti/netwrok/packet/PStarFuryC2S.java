package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Modifier.StarFury;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PStarFuryC2S {
    public PStarFuryC2S() {

    }

    public PStarFuryC2S(FriendlyByteBuf buf) {
    }

    public void toByte(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        if (supplier.get().getSender() != null) {
            context.enqueueWork(() -> {
                ServerPlayer serverPlayer = supplier.get().getSender();
                if (serverPlayer != null) {
                    StarFury.summonMeteor(serverPlayer);
                }
            });
        }
        return true;
    }
}
