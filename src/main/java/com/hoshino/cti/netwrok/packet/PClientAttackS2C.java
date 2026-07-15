package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PClientAttackS2C {
    public PClientAttackS2C(){
    }
    public PClientAttackS2C(FriendlyByteBuf buf){
    }
    public void toByte(FriendlyByteBuf byteBuf){
    }
    public void handle(Supplier<NetworkEvent.Context> supplier){
        var player = SafeClientAccess.getPlayer();
        if (player!=null){
            Minecraft mc = Minecraft.getInstance();
            var hitResult = mc.hitResult;
            var hitType = hitResult.getType();
            var gm = mc.gameMode;
            switch(hitType) {
                case ENTITY: {
                    gm.attack(player, ((EntityHitResult) hitResult).getEntity());
                    break;
                }
                case MISS,BLOCK: {
                    if (gm.hasMissTime()) {
                        mc.missTime = 10;
                    }
                    ForgeHooks.onEmptyLeftClick(player);
                }
            }
            player.swing(InteractionHand.MAIN_HAND);
        }
    }
}
