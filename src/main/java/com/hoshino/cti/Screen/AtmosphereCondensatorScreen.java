package com.hoshino.cti.Screen;

import com.hoshino.cti.Screen.menu.AtmosphereCondensatorMenu;
import com.hoshino.cti.util.BiomeUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.hoshino.cti.util.BiomeUtil.getBiomeKey;

public class AtmosphereCondensatorScreen extends AbstractContainerScreen<AtmosphereCondensatorMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("cti:textures/gui/machine/gui_condensor.png");
    private static final int TEXTURE_SIZE = 16;
    public static Font font = Minecraft.getInstance().font;
    public static final NumberFormat nf = NumberFormat.getIntegerInstance();

    public AtmosphereCondensatorScreen(AtmosphereCondensatorMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }


    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        drawProgress(poseStack, x, y);
        drawFE(poseStack, x, y);
    }

    protected void drawProgress(PoseStack poseStack, int x, int y) {
        if (menu.isCrafting()) {
            int $$4 = this.leftPos;
            int $$5 = this.topPos;
            int $$7 = menu.getProgressScale();
            blit(poseStack, $$4 + 80, $$5 + 34, 176, 14, $$7 + 1, 16);
        }
    }

    protected void drawFE(PoseStack poseStack, int x, int y) {
        int $$4 = this.leftPos;
        int $$5 = this.topPos;
        int $$7 = menu.getEnergyBarScale();
        blit(poseStack, $$4 + 62, $$5 + 67, 176, 31, $$7, 12);
    }

    @Override
    public void render(PoseStack poseStack, int mousex, int mousey, float delta) {
        int leftPos1 = this.leftPos;
        int topPos1 = this.topPos;

        renderBackground(poseStack);
        super.render(poseStack, mousex, mousey, delta);
        renderTooltip(poseStack, mousex, mousey);
        poseStack.pushPose();
        {
            poseStack.translate(leftPos1 + 116, topPos1 + 1, 0);
            drawFluid(poseStack, 16, this.menu.getFluidstack());
        }
        poseStack.popPose();
        if (menu.entity.getLevel() != null) {
            drawString(poseStack, font, Component.literal("群系: ").append(Component.translatable(BiomeUtil.BiomekeyToString(getBiomeKey(menu.entity.getLevel().getBiome(menu.entity.getBlockPos()))))), leftPos1 + 8, topPos1 + 56, 0xDD00FF);
        }
        if (mousex >= leftPos1 + 62 && mousey >= topPos1 + 67 && mousex <= leftPos1 + 123 && mousey <= topPos1 + 80) {
            drawString(poseStack, font, String.valueOf(menu.getEnergy()) + "FE", mousex + 8, mousey - 12, 0xff6000);
            drawString(poseStack, font, "最大: " + String.valueOf(menu.entity.getMaxEnergy()) + "FE", mousex + 8, mousey, 0xff6000);
            drawString(poseStack, font, "最大: " + String.valueOf(menu.entity.getEnergyPerTick()) + "FE/t", mousex + 8, mousey + 12, 0xff6000);
        }
        if (mousex >= leftPos1 + 116 && mousey >= topPos1 + 22 && mousex <= leftPos1 + 132 && mousey <= topPos1 + 62) {
            List<Component> ls0 = getTooltip(this.menu.getFluidstack(), TooltipFlag.Default.NORMAL);
            if (!ls0.isEmpty()) {
                int i = 0;
                int b = ls0.size();
                while (i < b) {
                    drawString(poseStack, font, ls0.get(i), mousex + 8, mousey - 12 + 8 * i, 0xAAff00);
                    i++;
                }
            }
        }
    }

    public void drawFluid(PoseStack poseStack, final int width, FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        int fluidColor = getColorTint(fluidStack);
        if (fluid.isSame(Fluids.EMPTY)) {
            return;
        }
        TextureAtlasSprite fluidSprite = getFluidStillSprite(fluidStack);
        int height = this.menu.getFluidBarScale();
        if (height <= 0) {
            height = 1;
        }
        if (height >= 60) {
            height = 60;
        }
        drawTiledSprite(poseStack, 16, 60, fluidColor, height, fluidSprite);

    }

    public TextureAtlasSprite getFluidStillSprite(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation fluidStill = renderProperties.getStillTexture();
        Minecraft mc = Minecraft.getInstance();
        return mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
    }

    public int getColorTint(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        return renderProperties.getTintColor();
    }

    private static void setGLCoLorFromInt(int coLor) {
        float red = (coLor >> 16 & 0xFF) / 255.0F;
        float green = (coLor >> 8 & 0xFF) / 255.0F;
        float bLue = (coLor & 0xFF) / 255.0F;
        float alpha = ((coLor >> 24) & 0xFF) / 255F;
        RenderSystem.setShaderColor(red, green, bLue, alpha);
    }

    private static void drawTiledSprite(PoseStack poseStack, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = poseStack.last().pose();
        setGLCoLorFromInt(color);
        final int xTileCount = tiledWidth / TEXTURE_SIZE;
        final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
        final long yTileCount = scaledAmount / TEXTURE_SIZE;
        final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);
        final int yStart = tiledHeight;
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
                long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
                int x = (xTile * TEXTURE_SIZE);
                int y = yStart - ((yTile + 1) * TEXTURE_SIZE);
                if (width > 0 && height > 0) {
                    long maskTop = TEXTURE_SIZE - height;
                    int maskRight = TEXTURE_SIZE - width;
                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }

    public List<Component> getTooltip(FluidStack fluidStack, TooltipFlag tooltipFlag) {
        List<Component> tooltip = new ArrayList<>();

        if (fluidStack.isEmpty()) {
            return tooltip;
        }

        Component displayName = fluidStack.getDisplayName();
        tooltip.add(displayName);
        long amount = fluidStack.getAmount();
        long milliBuckets = (amount * 1000) / FluidType.BUCKET_VOLUME;
        if (Screen.hasShiftDown()) {
            MutableComponent amountString = Component.translatable("cti.tooltip.liquid.amount.with.capacity", nf.format(milliBuckets), nf.format(this.menu.entity.FLUID_TANK.getCapacity()));
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
        } else {
            MutableComponent amountString = Component.translatable("cti.tooltip.liquid.amount", nf.format(milliBuckets));
            tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
        }
        return tooltip;
    }


}





