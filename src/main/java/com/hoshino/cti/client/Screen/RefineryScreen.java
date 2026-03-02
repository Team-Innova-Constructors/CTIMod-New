package com.hoshino.cti.client.Screen;

import com.hoshino.cti.Blocks.BlockEntity.tinker.refinery.RefineryControllerBlockEntity;
import com.hoshino.cti.Cti;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.tconstruct.smeltery.client.screen.HeatingStructureScreen;
import slimeknights.tconstruct.smeltery.menu.HeatingStructureContainerMenu;

import java.util.List;

public class RefineryScreen extends HeatingStructureScreen {
    public final RefineryControllerBlockEntity blockEntity;
    public RefineryScreen(HeatingStructureContainerMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        if (container.getTile() instanceof RefineryControllerBlockEntity be)
            this.blockEntity = be;
        else this.blockEntity = null;
    }
    public static final ResourceLocation PRESSURE_GAUGE = Cti.getResource("textures/gui/machine/refinery_pressure_gauge.png");

    @Override
    protected void renderBg(PoseStack matrices, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(matrices, partialTicks, mouseX, mouseY);
        if (this.blockEntity!=null) {
            RenderSystem.setShaderTexture(0, PRESSURE_GAUGE);
            Screen.blit(matrices, 84+leftPos, topPos, 0, 0, 26, 79, 256, 256);
            int pressureHeight = (int) (36*(Math.min(0,blockEntity.getPressure())/-1f));
            Screen.blit(matrices, 93+leftPos, 32+topPos, 26, 0, 6, pressureHeight, 256, 256);
        }
    }

    @Override
    protected void renderTooltip(PoseStack matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);
        if (this.blockEntity!=null&&mouseX>=90+leftPos&&mouseX<=102+leftPos&&mouseY>=6+topPos&&mouseY<=75+topPos){
            List<Component> tooltips = List.of(
                    Component.translatable("gui.cti.tooltip.pressure").append(": "+String.format("%.1f",blockEntity.getPressure())+" bar"),
                    Component.translatable("gui.cti.tooltip.refinery_pressure_boost")
            );
            this.renderComponentTooltip(matrices,tooltips,mouseX,mouseY);
        }
    }
}
