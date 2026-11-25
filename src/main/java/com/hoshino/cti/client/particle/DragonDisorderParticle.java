package com.hoshino.cti.client.particle;

import com.c2h6s.etshtinker.client.particle.LightningStrikeParticle;
import com.hoshino.cti.Cti;
import com.hoshino.cti.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DragonDisorderParticle extends Particle {
    public List<Vec3> offsets = new ArrayList<>();
    private Vec3 velocity;
    public float radius = 0.03f;
    DragonDisorderParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
        super(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed);
        this.lifetime = 6;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        Random random1 = new Random();
        velocity = new Vec3(pXSpeed,pYSpeed,pZSpeed);
        var offsetDirection = velocity.cross(new Vec3(random1.nextDouble()-0.5,random1.nextDouble()-0.5,random1.nextDouble()-0.5)).normalize();
        if (velocity.length()<=0.05f) offsets.clear();
        else if (velocity.length()<=0.4){
            offsetDirection = offsetDirection.scale(0.1f);
            for (int i = 0; i < velocity.length()/0.05f; i++) {
                offsets.add(offsetDirection.scale(random1.nextDouble()-0.5)
                        .add(new Vec3(random1.nextDouble()-0.5,random1.nextDouble()-0.5,random1.nextDouble()-0.5))
                        .normalize().scale(offsetDirection.length()*0.15f));
            }
        } else {
            offsetDirection = offsetDirection.scale(velocity.length()/6);
            for (int i = 0; i < 8; i++) {
                offsets.add(offsetDirection.scale(random1.nextDouble()-0.5)
                        .add(new Vec3(random1.nextDouble()-0.5,random1.nextDouble()-0.5,random1.nextDouble()-0.5))
                        .normalize().scale(offsetDirection.length()*0.15f));
            }
        }
    }

    @Override
    public void move(double pX, double pY, double pZ) {
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        if (!offsets.isEmpty()) {
            Vec3 pos = this.getPos().subtract(pRenderInfo.getPosition());
            float ticks = this.age + pPartialTicks;
            float percRadius =  ticks/this.lifetime;
            float alpha =Math.max(0, (this.lifetime-ticks)/this.lifetime);
            float b = 1;
            float g = 0;
            float r = 0.4f;
            percRadius*=0.25f;
            percRadius+=0.75f;
            float percOffset = ticks/this.lifetime;
            var nodes = new ArrayList<Vec3>();
            var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            nodes.add(Vec3.ZERO);
            Vec3 direction = velocity.normalize().scale(velocity.length() / (offsets.size()+1));
            for (int i = 0; i < offsets.size(); i++) {
                var offset = offsets.get(i).scale(1+percOffset);
                nodes.add(direction.scale(i).add(offset));
            }
            nodes.add(velocity);
            if (percRadius > 0) {
                var poseStack = new PoseStack();
                poseStack.translate(pos.x,pos.y,pos.z);
                RenderUtil.drawFoldedPipe(bufferSource.getBuffer(RenderUtil.brightProjectileRenderType(Cti.getResource("textures/particle/blank.png"))),poseStack, nodes , percRadius * this.radius, FastColor.ARGB32.color((int) (255*alpha),(int) (255*r), (int) (255*g), (int) (255*b)), 3);
                RenderUtil.drawFoldedPipe(bufferSource.getBuffer(RenderUtil.brightProjectileRenderType(Cti.getResource("textures/particle/blank.png"))),poseStack, nodes , this.radius*alpha, 0xffffffff, 3);
                bufferSource.endBatch();
            }
        }
    }

    public Vec3 getPos() {
        return new Vec3(x,y,z);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Provider() {}
        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DragonDisorderParticle(pLevel,pX,pY,pZ,pXSpeed,pYSpeed,pZSpeed);
        }
    }
}
