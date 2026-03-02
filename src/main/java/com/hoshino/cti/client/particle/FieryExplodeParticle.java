package com.hoshino.cti.client.particle;

import com.c2h6s.etshtinker.client.particle.annhilParticle;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class FieryExplodeParticle extends TextureSheetParticle {
    public static FieryExplodeParticle.FieryExplodeParticleProvider provider(SpriteSet spriteSet) {
        return new FieryExplodeParticle.FieryExplodeParticleProvider(spriteSet);
    }

    public static class FieryExplodeParticleProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public FieryExplodeParticleProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FieryExplodeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }

    private final SpriteSet spriteSet;

    public FieryExplodeParticle(ClientLevel world, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteSet) {
        super(world, x, y, z);
        this.spriteSet = spriteSet;
        this.setSize(1f, 1f);
        this.quadSize =1.25f+EtSHrnd().nextFloat()*0.25f;
        this.lifetime = 6;
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
        this.setSprite(this.spriteSet.get((int) Math.min(12,(this.age+pPartialTicks)*2),12));
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
