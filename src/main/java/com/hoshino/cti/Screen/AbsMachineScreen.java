package com.hoshino.cti.Screen;
/*
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AbsMachineScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft:textures/gui/container/furnace.png");
    public AbsMachineScreen(Object p_97741_, Inventory p_97742_, Component p_97743_) {
        super((T) p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0,TEXTURE);
        int x = (width-imageWidth)/2;
        int y = (height-imageHeight)/2;

        this.blit(poseStack,x,y,0,0,imageWidth,imageHeight);

        drawProgress(poseStack,x,y);
    }

    protected abstract void drawProgress(PoseStack poseStack,int x,int y);
}
*/