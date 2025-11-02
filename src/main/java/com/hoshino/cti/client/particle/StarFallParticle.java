package com.hoshino.cti.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import lombok.Getter;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Getter
public class StarFallParticle extends TextureSheetParticle {
    private final Vec3 origin;
    private final float speed;
    private final float maxRadius;
    private final float currentRadius;

    public StarFallParticle(ClientLevel pLevel, double pX, double pY, double pZ, int alpha, int color, float speed, float size, float maxRadius, Vec3 origin) {
        super(pLevel, pX, pY, pZ);
        this.lifetime = 80;
        float r = (color >> 16) & 0xFF;
        float g = (color >> 8) & 0xFF;
        float b = color & 0xFF;
        this.setColor(r / 255f, g / 255f, b / 255f);
        this.hasPhysics = false;
        this.origin = origin;
        this.speed = speed;
        this.maxRadius = maxRadius;
        this.currentRadius = 0;
        this.quadSize = size;
        this.alpha= alpha;
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize = this.quadSize +1;
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        double interpolatedX = Mth.lerp(pPartialTicks, this.xo, this.x);
        double interpolatedY = Mth.lerp(pPartialTicks, this.yo, this.y);
        double interpolatedZ = Mth.lerp(pPartialTicks, this.zo, this.z);
        Vec3 cameraPos = pRenderInfo.getPosition();
        float translateX = (float) (interpolatedX - cameraPos.x());
        float translateY = (float) (interpolatedY - cameraPos.y());
        float translateZ = (float) (interpolatedZ - cameraPos.z());
        Quaternion baseRotation = Vector3f.YP.rotationDegrees(90.0F);
        if (this.roll != 0.0F) {
            float rollAngle = Mth.lerp(pPartialTicks, this.oRoll, this.roll);
            baseRotation.mul(Vector3f.ZP.rotation(rollAngle));
        }
        float quadSize = this.getQuadSize(pPartialTicks);
        Vector3f[] baseVertices = new Vector3f[]{
                new Vector3f(-1.0F, 0.0F, -1.0F),
                new Vector3f(-1.0F, 0.0F, 1.0F),
                new Vector3f(1.0F, 0.0F, 1.0F),
                new Vector3f(1.0F, 0.0F, -1.0F)
        };
        Vector3f[] transformedVertices = new Vector3f[4];
        for (int i = 0; i < 4; ++i) {
            Vector3f transformed = new Vector3f(baseVertices[i].x(),baseVertices[i].y(),baseVertices[i].z());
            transformed.transform(baseRotation);
            transformed.mul(quadSize);
            transformed.add(translateX, translateY, translateZ);
            transformedVertices[i] = transformed;
        }
        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        int packedLight = this.getLightColor(pPartialTicks);
        addVertex(pBuffer, transformedVertices[0], u1, v1, packedLight);
        addVertex(pBuffer, transformedVertices[1], u1, v0, packedLight);
        addVertex(pBuffer, transformedVertices[2], u0, v0, packedLight);
        addVertex(pBuffer, transformedVertices[3], u0, v1, packedLight);
        addVertex(pBuffer, transformedVertices[3], u0, v1, packedLight);
        addVertex(pBuffer, transformedVertices[2], u0, v0, packedLight);
        addVertex(pBuffer, transformedVertices[1], u1, v0, packedLight);
        addVertex(pBuffer, transformedVertices[0], u1, v1, packedLight);
    }

    private void addVertex(VertexConsumer buffer, Vector3f pos, float u, float v, int light) {
        buffer.vertex(pos.x(), pos.y(), pos.z())
                .uv(u, v)
                .color(this.rCol, this.gCol, this.bCol, this.alpha)
                .uv2(light)
                .endVertex();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
}
