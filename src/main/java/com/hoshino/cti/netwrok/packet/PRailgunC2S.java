package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Entity.Projectiles.TinkerRailgunProjectile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PRailgunC2S {
    private final int projectileID;

    public PRailgunC2S(int id) {
        this.projectileID = id;
    }

    public PRailgunC2S(FriendlyByteBuf buf) {
        this.projectileID = buf.readInt();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeInt(this.projectileID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel serverLevel = player.getLevel();
                Entity entity = serverLevel.getEntity(this.projectileID);
                if (entity instanceof TinkerRailgunProjectile projectile) {
                    projectile.sendItemS2CPacket();
                }
            }
        });
        return true;
    }
}
