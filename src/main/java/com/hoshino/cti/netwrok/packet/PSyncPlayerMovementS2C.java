package com.hoshino.cti.netwrok.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PSyncPlayerMovementS2C {
    public final Vec3 movement;
    public final Vec3 pos;

    public PSyncPlayerMovementS2C(Vec3 movement, Vec3 pos) {
        this.movement = movement;
        this.pos = pos;
    }
    public PSyncPlayerMovementS2C(FriendlyByteBuf buf){
        this.movement = new Vec3(buf.readDouble(),buf.readDouble(),buf.readDouble());
        this.pos = new Vec3(buf.readDouble(),buf.readDouble(),buf.readDouble());
    }
    public void toByte(FriendlyByteBuf byteBuf){
        byteBuf.writeDouble(movement.x);
        byteBuf.writeDouble(movement.y);
        byteBuf.writeDouble(movement.z);
        byteBuf.writeDouble(pos.x);
        byteBuf.writeDouble(pos.y);
        byteBuf.writeDouble(pos.z);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var player = SafeClientAccess.getPlayer();
        if (player != null) {
            player.setPos(pos);
            player.setDeltaMovement(movement);
        }
    }
}
