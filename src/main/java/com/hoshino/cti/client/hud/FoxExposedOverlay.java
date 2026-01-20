package com.hoshino.cti.client.hud;

import com.hoshino.cti.Cti;
import com.hoshino.cti.client.cache.ExposedDelay;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class FoxExposedOverlay {
    private static final ResourceLocation EXPOSED1 = Cti.getResource("textures/gui/exposed/exposed1.png");
    private static final ResourceLocation EXPOSED2 = Cti.getResource("textures/gui/exposed/exposed2.png");
    private static final ResourceLocation EXPOSED3 = Cti.getResource("textures/gui/exposed/exposed3.png");
    private static final ResourceLocation EXPOSED4 = Cti.getResource("textures/gui/exposed/exposed4.png");
    private static final int FULL_TEXTURE_WIDTH = 101;
    private static final int FULL_TEXTURE_HEIGHT = 34;
    public static final IGuiOverlay EXPOSED_OVERLAY = ((gui, poseStack, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }
        if(ExposedDelay.getExposedTick()>0){
            var array=new ResourceLocation[]{EXPOSED1,EXPOSED2,EXPOSED3,EXPOSED4};
            int index= ExposedDelay.getTextureIndex();
            if(index>3)return;
            var currentTexture=array[index];
            final int DISPLAY_WIDTH = 100;
            final int DISPLAY_HEIGHT = 33;
            int x = width / 2 - DISPLAY_WIDTH / 2;
            int y = 20;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, currentTexture);
            GuiComponent.blit(poseStack, x, y, 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, FULL_TEXTURE_WIDTH, FULL_TEXTURE_HEIGHT);
        }
    });
}
