package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.cache.SevenCurse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerCursePacket {
    private final int punishTime;
    private final int punishFre;
    private final int resoluteTime;

    public ServerCursePacket(int punishTime, int punishFre, int resoluteTime) {
        this.punishTime = punishTime;
        this.punishFre = punishFre;
        this.resoluteTime = resoluteTime;
    }

    public ServerCursePacket(FriendlyByteBuf buf) {
        this.punishTime = buf.readInt();
        this.punishFre = buf.readInt();
        this.resoluteTime = buf.readInt();
    }
    public void ToByte(FriendlyByteBuf buf) {
        buf.writeInt(punishTime);
        buf.writeInt(punishFre);
        buf.writeInt(resoluteTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            SevenCurse.setPunishTime(punishTime);
            SevenCurse.setPunishFre(punishFre);
            SevenCurse.setResoluteTime(resoluteTime);
        });
        return true;
    }
}
