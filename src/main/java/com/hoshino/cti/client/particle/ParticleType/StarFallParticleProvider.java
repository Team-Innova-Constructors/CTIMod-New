package com.hoshino.cti.client.particle.ParticleType;


import com.hoshino.cti.client.particle.StarFallParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StarFallParticleProvider implements ParticleProvider<StarFallParticleType> {
    private final SpriteSet spriteSet;

    public StarFallParticleProvider(SpriteSet spriteSet) {
        this.spriteSet = spriteSet;
    }

    @Override
    public @Nullable Particle createParticle(StarFallParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        var starfall = new StarFallParticle(pLevel, pX, pY, pZ, pType.getAlpha(), pType.getColor(), pType.getSpeed(), pType.getSize(), pType.getMaxRadius(), pType.getOrigin());
        starfall.pickSprite(this.spriteSet);
        return starfall;
    }
}
