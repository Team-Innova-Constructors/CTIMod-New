package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Entity.Projectiles.TinkerRailgunProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PRailgunItemS2C {
    private final TinkerRailgunProjectile projectile;
    private final ItemStack stack;

    public PRailgunItemS2C(TinkerRailgunProjectile projectile, ItemStack stack) {
        this.projectile = projectile;
        this.stack = stack;
    }

    public PRailgunItemS2C(FriendlyByteBuf buf) {
        if (Minecraft.getInstance().level != null) {
            this.projectile = (TinkerRailgunProjectile) Minecraft.getInstance().level.getEntity(buf.readInt());
        } else this.projectile = null;
        this.stack = buf.readItem();

    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeInt(this.projectile.getId());
        buf.writeItem(this.stack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (this.projectile != null) {
                this.projectile.stack = stack;
            }
        });
        return true;
    }
}
