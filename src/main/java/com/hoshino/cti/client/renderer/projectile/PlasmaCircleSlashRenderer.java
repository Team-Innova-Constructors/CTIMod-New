package com.hoshino.cti.client.renderer.projectile;

import com.c2h6s.etshtinker.etshtinker;
import com.hoshino.cti.Entity.Projectiles.PlasmaCircleSlashEntity;
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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PlasmaCircleSlashRenderer extends EntityRenderer<PlasmaCircleSlashEntity> {

    public PlasmaCircleSlashRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }
    @Override
    public void render(PlasmaCircleSlashEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount>=1&&pEntity.tickCount<=5&&pEntity.getEchoCount()==1){
            float scale = pEntity.getScale();
            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pEntity.getYRot()));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(-pEntity.getXRot()));
            pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(pEntity.angle));
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            VertexConsumer consumer =pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(getTextureLocation(pEntity,pPartialTick)));
            consumer.vertex(poseMatrix, -2*scale, -0.2f,-2*scale).color(255,255,255,255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, 2*scale,-0.2f, -2*scale).color(255,255,255,255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, 2*scale,-0.2f, 2*scale).color(255,255,255,255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, -2*scale, -0.2f,2*scale).color(255,255,255,255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            pPoseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(PlasmaCircleSlashEntity pEntity) {
        return getTextureLocation(pEntity,0);
    }

    public ResourceLocation getTextureLocation(PlasmaCircleSlashEntity entity,float partialTick) {
        int frame = Math.round( Mth.clamp((entity.tickCount+partialTick)*1.6f,1,8));
        return etshtinker.getResourceLoc(entity.getTexturePath()+frame+".png");
    }

    protected int getSkyLightLevel(PlasmaCircleSlashEntity p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(PlasmaCircleSlashEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }

}