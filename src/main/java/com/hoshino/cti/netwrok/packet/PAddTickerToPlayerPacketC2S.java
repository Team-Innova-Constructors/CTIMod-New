package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PAddTickerToPlayerPacketC2S {
    private final EntityTickerInstance ticker;

    public PAddTickerToPlayerPacketC2S(EntityTickerInstance ticker) {
        this.ticker = ticker;
    }
    public PAddTickerToPlayerPacketC2S(FriendlyByteBuf byteBuf){
        this.ticker = EntityTickerInstance.readFromNbt(byteBuf.readNbt(),byteBuf.readResourceLocation());
    }
    public void toByte(FriendlyByteBuf byteBuf){
        var nbt = new CompoundTag();
        ticker.writeToNbt(nbt);
        byteBuf.writeNbt(nbt);
        byteBuf.writeResourceLocation(ticker.ticker.getId());
    }
    public void handle(Supplier<NetworkEvent.Context> supplier){
        var player = supplier.get().getSender();
        if (player!=null)
            EntityTickerManager.getInstance(player).addTickerSimple(ticker);
    }
}
