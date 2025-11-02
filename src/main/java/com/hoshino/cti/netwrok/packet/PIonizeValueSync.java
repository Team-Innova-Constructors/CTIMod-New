package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.hud.EnvironmentalPlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PIonizeValueSync {
    private final float ionizeValue;
    private final double ionizeAddup;

    public PIonizeValueSync(float amount, double amount2) {
        this.ionizeValue = amount;
        this.ionizeAddup = amount2;
    }

    public PIonizeValueSync(FriendlyByteBuf buf) {
        this.ionizeValue = buf.readFloat();
        this.ionizeAddup = buf.readDouble();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeFloat(this.ionizeValue);
        buf.writeDouble(this.ionizeAddup);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            EnvironmentalPlayerData.setIonizeValue(this.ionizeValue, this.ionizeAddup);
        });
        return true;
    }
}
