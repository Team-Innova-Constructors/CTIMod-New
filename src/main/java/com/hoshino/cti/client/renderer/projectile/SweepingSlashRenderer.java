package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;

public class SweepingSlashRenderer extends EntityRenderer<Projectile> {
    int r;
    int g;
    int b;
    float scale;
    public SweepingSlashRenderer(EntityRendererProvider.Context pContext,int r,int g,int b,float scale) {
        super(pContext);
        this.r = r;
        this.g = g;
        this.b = b;
        this.scale = scale;
    }

    @Override
    public void render(Projectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount>1&&pEntity.tickCount<=9){
            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pEntity.getYRot()));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(-pEntity.getXRot()));
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            VertexConsumer consumer =pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(getTextureLocation(pEntity)));
            consumer.vertex(poseMatrix, -1*scale, -0.1f,-2*scale).color(r,g,b,255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, scale,-0.1f, -2*scale).color(r,g,b,255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, scale,-0.1f, 2*scale).color(r,g,b,255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, -1*scale, -0.1f,2*scale).color(r,g,b,255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            pPoseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile pEntity) {
        int animationTick = Mth.clamp(pEntity.tickCount-2,0,7);
        return new ResourceLocation("minecraft","textures/particle/sweep_"+animationTick+".png");
    }
}
