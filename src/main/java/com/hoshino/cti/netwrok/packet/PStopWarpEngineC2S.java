package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PStopWarpEngineC2S {
    public PStopWarpEngineC2S(){

    }

    public PStopWarpEngineC2S(FriendlyByteBuf buf){
    }

    public void toByte(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context =supplier.get();
        ServerPlayer serverPlayer = context.getSender();
        if (serverPlayer!=null) {
            var instance = EntityTickerManager.getInstance(serverPlayer);
            if (instance.hasTicker(CtiEntityTickers.WARP_ENGINE.get()))
                instance.removeTicker(CtiEntityTickers.WARP_ENGINE.get());
        }
        return true;
    }
}
