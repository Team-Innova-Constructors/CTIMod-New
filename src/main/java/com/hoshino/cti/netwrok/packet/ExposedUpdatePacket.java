package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.cache.ExposedDelay;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ExposedUpdatePacket {
    private final int textureIndex;
    private final int exposedTick;

    public ExposedUpdatePacket(int index, int tick) {
        this.textureIndex = index;
        this.exposedTick = tick;
    }

    public ExposedUpdatePacket(FriendlyByteBuf buf) {
        this.textureIndex = buf.readInt();
        this.exposedTick = buf.readInt();
    }

    public void ToByte(FriendlyByteBuf buf) {
        buf.writeInt(textureIndex);
        buf.writeInt(exposedTick);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ExposedDelay.setExposedTick(exposedTick);
            ExposedDelay.setTextureIndex(textureIndex);
        });
        return true;
    }
}
