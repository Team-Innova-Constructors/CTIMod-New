package com.hoshino.cti.client.particle.ParticleType;

import com.hoshino.cti.client.CtiParticleType;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import lombok.Getter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Getter
public class StarFallParticleType extends ParticleType<StarFallParticleType> implements ParticleOptions {
    private final int alpha;
    private final int color;
    private final float speed;
    private final float size;
    private final float maxRadius;
    private final Vec3 origin;

    public StarFallParticleType(boolean pOverrideLimiter, int alpha, int color, float speed, float size, float maxRadius, Vec3 origin) {
        super(pOverrideLimiter, DESERIALIZER);
        this.alpha = alpha;
        this.color = color;
        this.speed = speed;
        this.size = size;
        this.maxRadius = maxRadius;
        this.origin = origin;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return CtiParticleType.STARFALL.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(alpha);
        pBuffer.writeInt(color);
        pBuffer.writeFloat(speed);
        pBuffer.writeFloat(size);
        pBuffer.writeFloat(maxRadius);

        pBuffer.writeDouble(origin.x);
        pBuffer.writeDouble(origin.y);
        pBuffer.writeDouble(origin.z);
    }

    public static final Deserializer<StarFallParticleType> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull StarFallParticleType fromCommand(@NotNull ParticleType<StarFallParticleType> pParticleType, StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            int alpha = Mth.clamp(pReader.readInt(), 0, 255);
            pReader.expect(' ');
            int r = Mth.clamp(pReader.readInt(), 0, 255);
            pReader.expect(' ');
            int g = Mth.clamp(pReader.readInt(), 0, 255);
            pReader.expect(' ');
            int b = Mth.clamp(pReader.readInt(), 0, 255);
            pReader.expect(' ');


            float speed = pReader.readFloat();
            pReader.expect(' ');

            float size = pReader.readFloat();
            pReader.expect(' ');

            float maxRadius = pReader.readFloat();
            pReader.expect(' ');

            double x = pReader.readDouble();
            pReader.expect(' ');
            double y = pReader.readDouble();
            pReader.expect(' ');
            double z = pReader.readDouble();

            int rgb = (r << 16) | (g << 8) | b;
            Vec3 origin = new Vec3(x, y, z);

            return new StarFallParticleType(true, rgb, alpha, speed, size, maxRadius, origin);
        }

        @Override
        public @NotNull StarFallParticleType fromNetwork(@NotNull ParticleType<StarFallParticleType> pParticleType, FriendlyByteBuf pBuffer) {
            int alpha = pBuffer.readInt();
            int color = pBuffer.readInt();
            float speed = pBuffer.readFloat();
            float size = pBuffer.readFloat();
            float maxRadius = pBuffer.readFloat();

            double x = pBuffer.readDouble();
            double y = pBuffer.readDouble();
            double z = pBuffer.readDouble();

            Vec3 origin = new Vec3(x, y, z);
            return new StarFallParticleType(true, alpha, color, speed, size, maxRadius, origin);
        }
    };


    @Override
    public @NotNull String writeToString() {
        return String.format("%d %d %d %f",
                (color >> 16) & 0xFF,
                (color >> 8) & 0xFF,
                color & 0xFF,
                speed);
    }

    @Override
    public @NotNull Codec<StarFallParticleType> codec() {
        return Codec.unit(new StarFallParticleType(true, 100, 0xffaaff, speed, size, maxRadius, origin));
    }
}
