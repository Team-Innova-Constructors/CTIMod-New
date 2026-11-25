package com.hoshino.cti.client.event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.client.cache.EjianClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EjianClientEvents {

    private static final ResourceLocation MARK_TEXTURE = Cti.getResource("textures/effect_overlay/ejian.png");
    private static final float MARK_SIZE = 0.42F;
    private static final float MARK_OFFSET = 0.45F;
    private static final int MARK_ALPHA = 128;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        EjianClient.tick();
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<Player, ?> event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!EjianClient.isActive(player.getUUID())) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == player && minecraft.options.getCameraType().isFirstPerson()) {
            return;
        }
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        double centerY = player.getY() + player.getBbHeight() * 0.5D;
        Vec3 toCamera = new Vec3(cameraPos.x - player.getX(), cameraPos.y - centerY, cameraPos.z - player.getZ());
        if (toCamera.lengthSqr() > 1.0E-4D) {
            toCamera = toCamera.normalize().scale(MARK_OFFSET);
        }
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();
        poseStack.pushPose();
        poseStack.translate(toCamera.x, player.getBbHeight() * 0.5D + toCamera.y, toCamera.z);
        poseStack.mulPose(camera.rotation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(MARK_TEXTURE));
        consumer.vertex(matrix, -MARK_SIZE, -MARK_SIZE, 0.0F).color(255, 255, 255, MARK_ALPHA).uv(0.0F, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, -MARK_SIZE, MARK_SIZE, 0.0F).color(255, 255, 255, MARK_ALPHA).uv(0.0F, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, MARK_SIZE, MARK_SIZE, 0.0F).color(255, 255, 255, MARK_ALPHA).uv(1.0F, 0.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix, MARK_SIZE, -MARK_SIZE, 0.0F).color(255, 255, 255, MARK_ALPHA).uv(1.0F, 1.0F)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BRIGHT).normal(poseStack.last().normal(), 0.0F, 1.0F, 0.0F).endVertex();
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        EjianClient.clear();
    }
}
