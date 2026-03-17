package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.util.ParticleContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

public class PAddParticleS2C {
    public final ParticleContext particleContext;

    public PAddParticleS2C(ParticleContext particleContext) {
        this.particleContext = particleContext;
    }
    public PAddParticleS2C(FriendlyByteBuf byteBuf){
        this.particleContext = ParticleContext.fromByteBuf(byteBuf);
    }
    public void toByte(FriendlyByteBuf byteBuf){
        particleContext.toByteBuf(byteBuf);
    }
    public void handle(Supplier<NetworkEvent.Context> supplier){
        Level level = SafeClientAccess.getLevel();
        if (level!=null){
            particleContext.addToLevel(level);
        }
    }
}
