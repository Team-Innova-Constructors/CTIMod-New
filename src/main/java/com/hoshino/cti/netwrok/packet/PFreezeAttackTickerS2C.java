package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.util.mixin.IPlayerMixin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PFreezeAttackTickerS2C {
    public PFreezeAttackTickerS2C() {
    }

    public PFreezeAttackTickerS2C(FriendlyByteBuf buf) {
    }

    public void toByte(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        var player = SafeClientAccess.getPlayer();
        if (player!=null){
            ((IPlayerMixin)player).cti$preventNextAttackTickerReset();
        }
        return true;
    }
}
