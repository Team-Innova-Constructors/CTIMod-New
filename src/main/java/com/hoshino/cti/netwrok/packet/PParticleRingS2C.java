package com.hoshino.cti.netwrok.packet;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

import static com.hoshino.cti.util.ParticleContext.readParticleOption;

@RequiredArgsConstructor
public class PParticleRingS2C {
    private final float radius;
    private final float diffuseVelocity;
    private final Vec3 pos;
    private final ParticleOptions options;
    private final float perDeg;

    public PParticleRingS2C(FriendlyByteBuf byteBuf){
        this(byteBuf.readFloat(),byteBuf.readFloat(),new Vec3(byteBuf.readFloat(),byteBuf.readFloat(),byteBuf.readFloat()),readParticleOption(byteBuf.readById(Registry.PARTICLE_TYPE),byteBuf),byteBuf.readFloat());
    }

    public void toByte(FriendlyByteBuf byteBuf){
        byteBuf.writeFloat(radius);
        byteBuf.writeFloat(diffuseVelocity);
        byteBuf.writeFloat((float) pos.x);
        byteBuf.writeFloat((float) pos.y);
        byteBuf.writeFloat((float) pos.z);
        byteBuf.writeId(Registry.PARTICLE_TYPE, options.getType());
        options.writeToNetwork(byteBuf);
        byteBuf.writeFloat(perDeg);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        var level = SafeClientAccess.getLevel();
        if (level!=null){
            for (float i = 0; i < 360; i+=perDeg) {
                var direction = new Vec3(Math.sin(Math.toRadians(i)),0,Math.cos(Math.toRadians(i))).normalize();
                var position = pos.add(direction.scale(radius));
                level.addParticle(options,position.x,position.y,position.z,direction.x*diffuseVelocity,0,direction.z*diffuseVelocity);
            }
        }
        return true;
    }
}
