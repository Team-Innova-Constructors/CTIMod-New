package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.Cti;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import static com.hoshino.cti.util.method.RenderUtils.drawPipe;

public class FieryJavelinRender extends EntityRenderer<Projectile> {
    public FieryJavelinRender(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(Projectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount>2) {
            pPoseStack.pushPose();
            Vec3 direction = pEntity.getDeltaMovement().normalize();
            double d0 = direction.horizontalDistance();
            float yRot = (float)(Mth.atan2(direction.x, direction.z) * 57.2957763671875);
            float xRot =  (float)(Mth.atan2(-direction.y, d0) * 57.2957763671875);
            pPoseStack.translate(0,pEntity.getBbHeight()/2,0);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(yRot));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(xRot));
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            float distance = (float) pEntity.getDeltaMovement().length();

            VertexConsumer consumer = pBuffer.getBuffer(RenderType.beaconBeam(getTextureLocation(pEntity),false));
            drawPipe(pPoseStack,consumer,poseMatrix,0.06f,distance,255,255,255,255,normalMatrix);
            consumer = pBuffer.getBuffer(RenderType.beaconBeam(getTextureLocation(pEntity),true));
            drawPipe(pPoseStack,consumer,poseMatrix,0.12f,distance,255,172,128,128,normalMatrix);

            pPoseStack.popPose();
        }
    }





    @Override
    protected int getBlockLightLevel(Projectile pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(Projectile pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile entity){
        return Cti.getResource("textures/particle/blank.png");
    }
}
