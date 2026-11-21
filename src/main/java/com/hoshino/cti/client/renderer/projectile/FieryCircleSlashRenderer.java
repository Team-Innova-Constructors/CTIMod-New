package com.hoshino.cti.client.renderer.projectile;

import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.etshtinker;
import com.hoshino.cti.Cti;
import com.hoshino.cti.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class FieryCircleSlashRenderer extends EntityRenderer<Projectile> {

    public FieryCircleSlashRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }
    @Override
    public void render(Projectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        if (pEntity.tickCount>=1&&pEntity.tickCount<8){
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            Player player = pEntity.getOwner() instanceof Player player1?player1:null;
            boolean firstPerson =camera.getEntity()==player&&Minecraft.getInstance().options.getCameraType().isFirstPerson();
            if (firstPerson){
                ClientLevel level = Minecraft.getInstance().level;
                if (level == null) return;

                var eyeOffset = new Vec3(0,(player.getBbHeight()/2)-player.getEyeHeight(),0);
                double x = Mth.lerp(pPartialTick, pEntity.xOld, pEntity.getX());
                double y = Mth.lerp(pPartialTick, pEntity.yOld, pEntity.getY());
                double z = Mth.lerp(pPartialTick, pEntity.zOld, pEntity.getZ());

                Vec3 finalpos;
                finalpos = camera.getPosition().add(eyeOffset);
                Vec3 offSet = finalpos.subtract(x,y,z);
                pPoseStack.translate(offSet.x,offSet.y,offSet.z);
            }

            float scale = 4;
            pPoseStack.pushPose();
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pEntity.getYRot()));
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(-pEntity.getXRot()));
            PoseStack.Pose pose = pPoseStack.last();
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            VertexConsumer consumer =pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(getTextureLocation(pEntity,pPartialTick)));
            consumer.vertex(poseMatrix, -2*scale, -0.15f,-2*scale).color(255,255,255,255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, 2*scale,-0.15f, -2*scale).color(255,255,255,255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, 2*scale,-0.15f, 2*scale).color(255,255,255,255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            consumer.vertex(poseMatrix, -2*scale, -0.15f,2*scale).color(255,255,255,255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(normalMatrix, 0, -1, 0).endVertex();
            pPoseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile pEntity) {
        return getTextureLocation(pEntity,0);
    }

    public ResourceLocation getTextureLocation(Projectile entity,float partialTick) {
        int frame = Math.round( Mth.clamp((entity.tickCount+partialTick)*1.25f,1,10));
        return Cti.getResource("textures/projectile/fiery_circle_slash/fiery_circle_slash"+frame+".png");
    }

    protected int getSkyLightLevel(Projectile p_114509_, BlockPos p_114510_) {
        return 15;
    }
    protected int getBlockLightLevel(Projectile p_114496_, BlockPos p_114497_) {
        return 15;
    }

}
