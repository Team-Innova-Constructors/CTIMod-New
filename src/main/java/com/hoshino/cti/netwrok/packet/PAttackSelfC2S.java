package com.hoshino.cti.netwrok.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

public class PAttackSelfC2S {
    public PAttackSelfC2S() {
    }

    public PAttackSelfC2S(FriendlyByteBuf buf) {
    }

    public void toByte(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ToolAttackUtil.attackEntity(ToolStack.from(player.getItemInHand(InteractionHand.MAIN_HAND)), player, InteractionHand.MAIN_HAND, player, () -> 1, false);
            }
        });
        return true;
    }
}
