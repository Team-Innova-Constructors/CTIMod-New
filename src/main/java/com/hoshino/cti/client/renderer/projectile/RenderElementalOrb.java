package com.hoshino.cti.client.renderer.projectile;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.Projectiles.base.BasicElementalOrbEntity;
import com.hoshino.cti.client.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class RenderElementalOrb extends EntityRenderer<BasicElementalOrbEntity> {
    private final int cR;
    private final int cG;
    private final int cB;
    public RenderElementalOrb(EntityRendererProvider.Context pContext, int cR, int cG, int cB) {
        super(pContext);
        this.cR = cR;
        this.cG = cG;
        this.cB = cB;
    }

    @Override
    public boolean shouldRender(BasicElementalOrbEntity entity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return super.shouldRender(entity,pCamera,pCamX,pCamY,pCamZ)||(entity.shouldRender(pCamX,pCamY,pCamZ)&&
                (entity.tickCount<10||entity.getOwner()==Minecraft.getInstance().player)
        );
    }

    @Override
    public void render(BasicElementalOrbEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        var count = 5f;
        var positions = pEntity.getPositionCache();
        List<Vec3> tailPositions = new ArrayList<>();

        if (positions.size()>=3) {
            var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            double x = Mth.lerp(pPartialTick,pEntity.xOld,pEntity.getX());
            double y = Mth.lerp(pPartialTick,pEntity.yOld,pEntity.getY());
            double z = Mth.lerp(pPartialTick,pEntity.zOld,pEntity.getZ());
            pPartialTick = (int)(count*pPartialTick) / count;
            var lookVec = camera.getLookVector();
            var renderPos = new Vec3(x,y,z);

            for (int i = 0; i < positions.size() - 2; i++) {
                var last = positions.get(i);
                var start = positions.get(i + 1);
                var end = positions.get(i + 2);
                var legacy = start.subtract(last).scale(1F / count);
                var direction = end.subtract(start).scale(1F / count);
                boolean shouldEnd = i == positions.size() - 3;

                for (int j = 1; j <= count; j++) {
                    if (shouldEnd && j > pPartialTick * count) break;
                    var pos = start.subtract(renderPos).add(legacy.scale( 1f - ((float) j / count)) ).add( direction.scale(j) );

                    tailPositions.add(pos);
                }
            }
            float unusedBound = tailPositions.size()-((positions.size()-1)*count);
            float unit = 1f / (tailPositions.size() - 1-Math.max(2,unusedBound));
            if (unit > 0) {

                var vertexConsumer = pBuffer.getBuffer(RenderUtil.brightProjectileRenderType(getTextureLocation(pEntity)));
                pPoseStack.pushPose();
                PoseStack.Pose pose = pPoseStack.last();
                Matrix3f normalMatrix = pose.normal();
                Matrix4f poseMatrix = pose.pose();
                for (int i = tailPositions.size() - 1; i >= Math.max(2,unusedBound) ; i--) {
                    var last = tailPositions.get(i - 2);
                    var start = tailPositions.get(i - 1);
                    var end = tailPositions.get(i);
                    var lookAngle = new Vec3(lookVec);
                    var sOffset = lookAngle.cross(start.subtract(last)).normalize().scale(0.2f*(i-1)*unit);
                    var eOffset = lookAngle.cross(end.subtract(start)).normalize().scale(0.2f*i*unit);
                    int a1 = (int) (255 * Math.min(1f, i * unit));
                    int a0 = (int) (255 * Math.min(1f, (i - 1f) * unit));
                    var plusStartOffset = start.add(sOffset);
                    var minusStartOffset = start.subtract(sOffset);
                    var plusEndOffset = end.add(eOffset);
                    var minusEndOffset = end.subtract(eOffset);
                    var plusStartOffsetW = start.add(sOffset.scale(0.25));
                    var minusStartOffsetW = start.subtract(sOffset.scale(0.25));
                    var plusEndOffsetW = end.add(eOffset.scale(0.25));
                    var minusEndOffsetW = end.subtract(eOffset.scale(0.25));
                    renderQuad(plusStartOffsetW, plusEndOffsetW, plusStartOffset, plusEndOffset,
                            FastColor.ARGB32.color(a0,cR,cG,cB), FastColor.ARGB32.color(0,cR,cG,cB),
                            FastColor.ARGB32.color(a1,cR,cG,cB), FastColor.ARGB32.color(0,cR,cG,cB),
                            normalMatrix,poseMatrix, vertexConsumer);
                    renderQuad(minusStartOffsetW, minusEndOffsetW, minusStartOffset, minusEndOffset,
                            FastColor.ARGB32.color(a0,cR,cG,cB), FastColor.ARGB32.color(0,cR,cG,cB),
                            FastColor.ARGB32.color(a1,cR,cG,cB), FastColor.ARGB32.color(0,cR,cG,cB),
                            normalMatrix,poseMatrix, vertexConsumer);
                    renderQuad(start, end, plusStartOffsetW, plusEndOffsetW,
                            FastColor.ARGB32.color(a0,255,255,255), FastColor.ARGB32.color(a0,cR,cG,cB),
                            FastColor.ARGB32.color(a1,255,255,255), FastColor.ARGB32.color(a1,cR,cG,cB),
                            normalMatrix,poseMatrix, vertexConsumer);
                    renderQuad(start, end, minusStartOffsetW, minusEndOffsetW,
                            FastColor.ARGB32.color(a0,255,255,255), FastColor.ARGB32.color(a0,cR,cG,cB),
                            FastColor.ARGB32.color(a1,255,255,255), FastColor.ARGB32.color(a1,cR,cG,cB),
                            normalMatrix,poseMatrix, vertexConsumer);
                }
                pPoseStack.popPose();
            }
        }
    }

    public void renderQuad(Vec3 start, Vec3 end, Vec3 offsetStart, Vec3 offsetEnd, int argbS0, int argbS1, int argbE0, int argbE1, Matrix3f normalMatrix, Matrix4f poseMatrix, VertexConsumer vertexConsumer){
        vertexConsumer.vertex(poseMatrix, (float) start.x, (float) start.y, (float) start.z)
                .color(argbS0).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, 0, 0).endVertex();
        vertexConsumer.vertex(poseMatrix, (float) end.x, (float) end.y, (float) end.z)
                .color(argbE0).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, 0, 1).endVertex();
        vertexConsumer.vertex(poseMatrix, (float) offsetEnd.x, (float) offsetEnd.y, (float) offsetEnd.z)
                .color(argbE1).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, 1, 1).endVertex();
        vertexConsumer.vertex(poseMatrix, (float) offsetStart.x, (float) offsetStart.y, (float) offsetStart.z)
                .color(argbS1).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, 0, 1, 0).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(BasicElementalOrbEntity pEntity) {
        return Cti.getResource("textures/particle/blank.png");
    }
}
