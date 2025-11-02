package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.Cti;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

public class FriendlyMeteorRenderer extends EntityRenderer<Projectile> {

    public FriendlyMeteorRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(Projectile pEntity, float p_114486_, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int p_114490_) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        PoseStack.Pose pose = pPoseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = pBuffer.getBuffer(RenderType.text(getTextureLocation(pEntity)));
        consumer.vertex(poseMatrix, -1, -1, 0).color(0xffffffff).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, 0, 0).endVertex();
        consumer.vertex(poseMatrix, 1, -1, 0).color(0xffffffff).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, 0, 1).endVertex();
        consumer.vertex(poseMatrix, 1, 1, 0).color(0xffffffff).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, 1, 1).endVertex();
        consumer.vertex(poseMatrix, -1, 1, 0).color(0xffffffff).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, 1, 0).endVertex();
        pPoseStack.popPose();
    }

    @Override
    protected int getBlockLightLevel(Projectile pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile meteorEntity) {
        return Cti.getResource("textures/projectile/nova_5.png");
    }
}
