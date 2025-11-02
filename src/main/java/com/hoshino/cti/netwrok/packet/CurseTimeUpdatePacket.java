package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.client.hud.CurseInfoHud;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CurseTimeUpdatePacket {
    private final int curseLevel;
    private final int curseTime;

    public CurseTimeUpdatePacket(int curseLevel, int curseTime) {
        this.curseLevel = curseLevel;
        this.curseTime = curseTime;
    }

    public CurseTimeUpdatePacket(FriendlyByteBuf buf) {
        this.curseLevel=buf.readInt();
        this.curseTime=buf.readInt();
    }
    public void ToByte(FriendlyByteBuf buf) {
        buf.writeInt(curseLevel);
        buf.writeInt(curseTime);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            CurseInfoHud.setCurseLevel(curseLevel);
            CurseInfoHud.setRemainingCurseTime(curseTime);
        });
        return true;
    }
}
