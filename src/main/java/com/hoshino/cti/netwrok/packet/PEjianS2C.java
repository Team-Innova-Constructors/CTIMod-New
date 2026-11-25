package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.cache.EjianClient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class PEjianS2C {

    private final UUID playerId;
    private final boolean active;
    private final int duration;

    public PEjianS2C(UUID playerId, boolean active, int duration) {
        this.playerId = playerId;
        this.active = active;
        this.duration = duration;
    }

    public PEjianS2C(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readBoolean(), buf.readVarInt());
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeBoolean(this.active);
        buf.writeVarInt(this.duration);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (this.active) {
                EjianClient.add(this.playerId, this.duration + 60);
            } else {
                EjianClient.remove(this.playerId);
            }
        });
        return true;
    }
}
