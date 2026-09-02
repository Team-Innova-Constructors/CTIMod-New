package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.RubyLaserEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import static com.hoshino.cti.client.util.RenderUtil.drawPipe;

public class RubyLaserRenderer extends EntityRenderer<RubyLaserEntity> {
    public RubyLaserRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public boolean shouldRender(RubyLaserEntity entity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        Vec3 vec3 = entity.position().add(entity.getDeltaMovement().scale(entity.getDataLength()));
        Vec3 vec32 = entity.position().add(entity.getDeltaMovement().scale(entity.getDataLength()/2f));
        Vec3 cameraPos = new Vec3(pCamX,pCamY,pCamZ);
        return entity.position().subtract(cameraPos).length()<64||vec3.subtract(cameraPos).length()<64||vec32.subtract(cameraPos).length()<64;
    }

    @Override
    public void render(RubyLaserEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        float distance = pEntity.getDataLength();
        if (distance>1&&pEntity.tickCount>2&&pEntity.readyToRender()) {
            pPoseStack.pushPose();
            Vec3 direction = pEntity.getDeltaMovement().normalize();
            double d0 = direction.horizontalDistance();
            float yRot = (float)(Mth.atan2(direction.x, direction.z) * 57.2957763671875);
            float xRot =  (float)(Mth.atan2(-direction.y, d0) * 57.2957763671875);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
            float scale = 1;
            pPoseStack.scale(scale, scale,1);
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();

            float tick = pEntity.tickCount+pPartialTick-2;
            float alphaPercent = Math.max(0,(7-tick)/7F);

            VertexConsumer consumer = pBuffer.getBuffer(RenderType.beaconBeam(getTextureLocation(pEntity),false));
            drawPipe(pPoseStack,consumer,poseMatrix,0.05f *alphaPercent,distance,255,255,255,255,normalMatrix);
            consumer = pBuffer.getBuffer(RenderType.beaconBeam(getTextureLocation(pEntity),true));
            drawPipe(pPoseStack,consumer,poseMatrix,0.1f,distance,255,0,0, (int) (128*alphaPercent),normalMatrix);

            pPoseStack.popPose();
        }
    }



    @Override
    protected int getBlockLightLevel(RubyLaserEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(RubyLaserEntity pEntity, BlockPos pPos) {
        return 15;
    }


    @Override
    public ResourceLocation getTextureLocation(RubyLaserEntity pEntity) {
        return Cti.getResource("textures/particle/blank.png");
    }
}
