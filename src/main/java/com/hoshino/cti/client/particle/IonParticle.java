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

    IonParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites){
        super(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed);
        this.lifetime = 15 + this.random.nextInt(5);
        this.xd = pXSpeed;
        this.yd = pYSpeed;
        this.zd = pZSpeed;
        this.friction = 0.5F;
        this.sprite = pSprites.get(this.random);
        this.hasPhysics = false;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 cameraPos = pRenderInfo.getPosition();
        Vec3 lookVec =(new Vec3(pRenderInfo.getLookVector())).reverse();
        float vx = (float) Mth.lerp(pPartialTicks, this.xdo, this.xd);
        float vy = (float)Mth.lerp(pPartialTicks, this.ydo, this.yd);
        float vz = (float)Mth.lerp(pPartialTicks, this.zdo, this.zd);
        Vec3 velocity = new Vec3(vx, vy, vz);
        Vec3 side = lookVec.cross(velocity).normalize().scale(0.05*velocity.length());
        float x = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - cameraPos.x());
        float y = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - cameraPos.y());
        float z = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - cameraPos.z());
        Vec3 pos = new Vec3(x,y,z);
        Vector3f[] positions = new Vector3f[]{
                new Vector3f(pos),
                new Vector3f(pos.subtract(velocity)),
                new Vector3f(pos.subtract(side).subtract(velocity)),
                new Vector3f(pos.subtract(side))
        };
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        pBuffer.vertex(positions[0].x(), positions[0].y(), positions[0].z()).uv(u1, v1).color(1f, 1, 1, this.alpha).uv2(LightTexture.FULL_BRIGHT).endVertex();
        pBuffer.vertex(positions[1].x(), positions[1].y(), positions[1].z()).uv(u1, v0).color(1f, 1, 1, this.alpha).uv2(LightTexture.FULL_BRIGHT).endVertex();
        pBuffer.vertex(positions[2].x(), positions[2].y(), positions[2].z()).uv(u0, v0).color(1f, 1, 1, this.alpha).uv2(LightTexture.FULL_BRIGHT).endVertex();
        pBuffer.vertex(positions[3].x(), positions[3].y(), positions[3].z()).uv(u0, v1).color(1f, 1, 1, this.alpha).uv2(LightTexture.FULL_BRIGHT).endVertex();
    }

    @Override
    public void tick() {
        this.age++;
        if (this.age>this.lifetime) this.remove();
        if (this.age>this.lifetime/2) this.alpha*=0.8f;
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
