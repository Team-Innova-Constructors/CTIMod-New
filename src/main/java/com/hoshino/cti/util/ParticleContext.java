package com.hoshino.cti.util;

import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PAddParticleS2C;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ParticleContext(ParticleOptions options, boolean pForceAlwaysRender, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
    public void toByteBuf(FriendlyByteBuf byteBuf){
        byteBuf.writeId(Registry.PARTICLE_TYPE, options.getType());
        options.writeToNetwork(byteBuf);
        byteBuf.writeBoolean(pForceAlwaysRender);
        byteBuf.writeDouble(pX);
        byteBuf.writeDouble(pY);
        byteBuf.writeDouble(pZ);
        byteBuf.writeDouble(pXSpeed);
        byteBuf.writeDouble(pYSpeed);
        byteBuf.writeDouble(pZSpeed);
    }
    public static ParticleContext fromByteBuf(FriendlyByteBuf byteBuf){
        return ParticleContext.buildParticle(readParticleOption(byteBuf.readById(Registry.PARTICLE_TYPE),byteBuf)).setAlwaysRender(byteBuf.readBoolean()).setPos(byteBuf.readDouble(),byteBuf.readDouble(),byteBuf.readDouble()).setVelocity(byteBuf.readDouble(),byteBuf.readDouble(),byteBuf.readDouble()).build();
    }
    public static <T extends ParticleOptions> T readParticleOption(ParticleType<T> particleType,FriendlyByteBuf byteBuf){
        return particleType.getDeserializer().fromNetwork(particleType,byteBuf);
    }
    public static Builder buildParticle(@NotNull ParticleOptions options){
        return new Builder(options);
    }

    public void addToLevel(Level level){
        level.addParticle(options,pForceAlwaysRender,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed);
    }
    public void sendToClient(ServerLevel serverLevel){
        serverLevel.players().forEach(serverPlayer ->
                CtiPacketHandler.sendToPlayer(new PAddParticleS2C(this),serverPlayer));
    }
    public static class Builder{
        ParticleOptions options;
        boolean pForceAlwaysRender;
        Vec3 pos;
        Vec3 velocity;
        public Builder(@NotNull ParticleOptions options){
            this.options = options;
        }

        public Builder setAlwaysRender(boolean forceAlwaysRender){
            this.pForceAlwaysRender = forceAlwaysRender;
            return this;
        }

        public Builder setPos(Vec3 pos) {
            this.pos = pos;
            return this;
        }
        public Builder setPos(double x, double y, double z){
            this.pos = new Vec3(x,y,z);
            return this;
        }
        public Builder randomizePos(double offset,RandomSource source){
            this.pos.add(new Vec3(source.nextDouble()*2-1,source.nextDouble()*2-1,source.nextDouble()*2-1).scale(offset));
            return this;
        }

        public Builder setVelocity(Vec3 velocity) {
            this.velocity = velocity;
            return this;
        }
        public Builder setVelocity(double x, double y, double z){
            this.velocity = new Vec3(x,y,z);
            return this;
        }
        public Builder setVelocity(double speed, RandomSource source){
            this.velocity = new Vec3(source.nextDouble()*2-1,source.nextDouble()*2-1,source.nextDouble()*2-1).scale(speed);
            return this;
        }
        public Builder randomizeVelocity(double offset,RandomSource source){
            this.velocity.add(new Vec3(source.nextDouble()*2-1,source.nextDouble()*2-1,source.nextDouble()*2-1).scale(offset));
            return this;
        }
        public ParticleContext build(){
            Objects.requireNonNull(pos,String.format("Particle position not set for particle context of particle type %s",this.options.writeToString()));
            Objects.requireNonNull(velocity,String.format("Particle velocity not set for particle context of particle type %s",this.options.writeToString()));
            return new ParticleContext(this.options,this.pForceAlwaysRender,this.pos.x,this.pos.y,this.pos.z,this.velocity.x,this.velocity.y,this.velocity.z);
        }
    }
}
