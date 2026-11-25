package com.hoshino.cti.client.hud;

import com.hoshino.cti.Cti;
import com.hoshino.cti.client.cache.EjianClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;


public class EjianOverlay {

    private static final ResourceLocation VIGNETTE = Cti.getResource("textures/gui/ejian/vignette.png");

    public static final IGuiOverlay VIGNETTE_OVERLAY = (gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }
        if (!EjianClient.isActive(minecraft.player.getUUID())) {
            return;
        }
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, VIGNETTE);
        GuiComponent.blit(poseStack, 0, 0, 0, 0, width, height, width, height);
        RenderSystem.disableBlend();
    };
}
