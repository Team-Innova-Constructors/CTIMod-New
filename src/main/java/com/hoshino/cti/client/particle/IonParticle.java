package com.hoshino.cti.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class IonParticle extends TextureSheetParticle {
    public static IonParticle.Provider provider(SpriteSet spriteSet) {
        return new IonParticle.Provider(spriteSet);
    }
    public double xdo;
    public double ydo;
    public double zdo;
    public double initialVelocity;

    IonParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites){
        super(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed);
        this.lifetime = 7 + this.random.nextInt(3);
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.initialVelocity = Mth.length(pXSpeed,pYSpeed,pZSpeed);
        this.friction = 0.9F;
        this.sprite = pSprites.get(this.random);
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        this.age++;
        if (this.age>this.lifetime) this.remove();
        if (this.age>this.lifetime/2) this.alpha*=0.8f;
        this.quadSize*=1.3f;
        this.xdo = this.xd;
        this.ydo = this.yd;
        this.zdo = this.zd;
        super.tick();
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new IonParticle(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed,this.sprites);
        }
    }
}
