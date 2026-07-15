package com.hoshino.cti.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class ManaStrikeParticle extends TextureSheetParticle {
    public static ManaStrikeParticle.ManaStrikeParticleProvider provider(SpriteSet spriteSet) {
        return new ManaStrikeParticle.ManaStrikeParticleProvider(spriteSet);
    }

    public static class ManaStrikeParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public ManaStrikeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ManaStrikeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    public ManaStrikeParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(1f, 1f);
        this.quadSize =1f+EtSHrnd().nextFloat()*0.2f;
        this.lifetime = 4;
        this.gravity = 0f;
        this.hasPhysics = false;
        this.xd = vx * 0;
        this.yd = vy * 0;
        this.zd = vz * 0;
        this.roll = (float)( EtSHrnd().nextFloat()*2*Math.PI);
        this.oRoll =this.roll;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if (this.age+pPartialTicks>3.125) return;
        this.setSprite(this.spriteSet.get((int) Math.min(5,(this.age+pPartialTicks)*1.6),5));
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    public int getLightColor(float p_234080_) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age>=this.lifetime){
            this.remove();
        }
    }
}
