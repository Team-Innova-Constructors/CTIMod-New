package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.hud.EnvironmentalPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PScorchValueSync {
    private final float Value;
    private final double Addup;

    public PScorchValueSync(float amount, double amount2) {
        this.Value = amount;
        this.Addup = amount2;
    }

    public PScorchValueSync(FriendlyByteBuf buf) {
        this.Value = buf.readFloat();
        this.Addup = buf.readDouble();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeFloat(this.Value);
        buf.writeDouble(this.Addup);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            EnvironmentalPlayerData.setScorchValue(this.Value, this.Addup);
        });
        return true;
    }
}
