package com.hoshino.cti.client.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.desht.pneumaticcraft.api.crafting.TemperatureRange;
import me.desht.pneumaticcraft.client.gui.widget.WidgetTemperature;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import slimeknights.mantle.client.screen.ElementScreen;
import slimeknights.mantle.client.screen.MultiModuleScreen;
import slimeknights.tconstruct.library.client.GuiUtil;
import slimeknights.tconstruct.library.client.RenderUtils;
import slimeknights.tconstruct.smeltery.block.controller.ControllerBlock;
import slimeknights.tconstruct.smeltery.block.entity.controller.HeatingStructureBlockEntity;
import slimeknights.tconstruct.smeltery.client.screen.HeatingStructureScreen;
import slimeknights.tconstruct.smeltery.client.screen.module.GuiFuelModule;
import slimeknights.tconstruct.smeltery.client.screen.module.GuiSmelteryTank;

import com.hoshino.cti.Blocks.BlockEntity.tinker.soulforge.SoulForgeControllerBlockEntity;
import com.hoshino.cti.Cti;
import com.hoshino.cti.Screen.menu.SoulForgeMenu;

import java.util.Objects;

/**
 * 熔魂炉 GUI：在最左（原侧边物品熔炼栏位置）绘制气压表 + 温度计，
 * 主体保留 Tinkers 冶炼炉的流体槽与燃料条，但不绘制任何熔炼物品相关组件。
 */
public class SoulForgeScreen extends MultiModuleScreen<SoulForgeMenu> {
    public static final ResourceLocation BACKGROUND = HeatingStructureScreen.BACKGROUND;
    public static final ElementScreen SCALA = new ElementScreen(176, 76, 52, 52, 256, 256);

    /** 与 Refinery 共用同一张气压表贴图 */
    public static final ResourceLocation PRESSURE_GAUGE = Cti.getResource("textures/gui/machine/soulforge_pressure_gauge.png");

    /** 气压表贴图区域：26x79，真空区高度按 |min(0,pressure)|/1.0 的比例填充 */
    private static final int PRESSURE_X = -60;          // 相对主面板左上
    private static final int PRESSURE_Y = 0;
    private static final int PRESSURE_W = 26;
    private static final int PRESSURE_H = 79;
    private static final int PRESSURE_FILL_INNER_X = 11;       // 在气压表贴图内的填充条 X 偏移
    private static final int PRESSURE_FILL_Y_BASE = 32;       // 与 RefineryScreen 保持一致
    private static final int PRESSURE_FILL_MAX_H = 36;

    private final SoulForgeControllerBlockEntity blockEntity;
    private final GuiSmelteryTank tank;
    private final GuiFuelModule fuel;
    private WidgetTemperature tempWidget;

    public SoulForgeScreen(SoulForgeMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        HeatingStructureBlockEntity te = container.getTile();
        if (te instanceof SoulForgeControllerBlockEntity soul) {
            this.blockEntity = soul;
            this.tank = new GuiSmelteryTank(this, te.getTank(), 8, 16, SCALA.w, SCALA.h,
                    Objects.requireNonNull(Registry.BLOCK_ENTITY_TYPE.getKey(te.getType())));
            this.fuel = new GuiFuelModule(this, te.getFuelModule(), 71, 32, 12, 36, 70, 15, false);
        } else {
            this.blockEntity = null;
            this.tank = null;
            this.fuel = null;
        }
    }

    @Override
    protected void init() {
        // 暂存旧的尺寸，避免 MultiModuleScreen.init() 之后因为下面 addRenderableWidget 导致重排错位
        super.init();
        if (blockEntity != null) {
            // 温度计放在气压表右侧（仍在主面板左外区）
            int wx = leftPos + PRESSURE_X + PRESSURE_W + 6;
            int wy = topPos + PRESSURE_Y + 18;
            tempWidget = new WidgetTemperature(wx, wy,
                    TemperatureRange.of(0, 273 + 200),  /* 0℃ ~ 200℃ 兜底量程，运行时 autoScale 会按当前温度重排 */
                    blockEntity.getHeatExchanger().getTemperatureAsInt(), 25);
            addRenderableWidget(tempWidget);
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (blockEntity == null) {
            return;
        }
        // 与 HeatingStructureScreen 一致：炉体结构无效或槽位数变化时关闭界面
        if (!blockEntity.getBlockState().getValue(ControllerBlock.IN_STRUCTURE)) {
            this.onClose();
            return;
        }
        if (tempWidget != null) {
            tempWidget.setTemperature(blockEntity.getHeatExchanger().getTemperatureAsInt());
            tempWidget.autoScaleForTemperature();
        }
    }

    @Override
    protected void renderBg(PoseStack matrices, float partialTicks, int mouseX, int mouseY) {
        GuiUtil.drawBackground(matrices, this, BACKGROUND);
        if (fuel != null) {
            fuel.draw(matrices);
        }
        super.renderBg(matrices, partialTicks, mouseX, mouseY);
        if (tank != null) {
            tank.renderFluids(matrices);
        }
        // 气压表：绘制在最左外区
        if (blockEntity != null) {
            RenderSystem.setShaderTexture(0, PRESSURE_GAUGE);
            Screen.blit(matrices, leftPos + PRESSURE_X, topPos + PRESSURE_Y, 0, 0, 63, 81, 256, 256);
            // 负压值越大，越接近真空；只有 min(0, pressure) 有意义
            int fillH = (int) (PRESSURE_FILL_MAX_H * (Math.min(0f, blockEntity.getPressure()) / -1f));
            if (fillH < 0) fillH = 0;
            if (fillH > PRESSURE_FILL_MAX_H) fillH = PRESSURE_FILL_MAX_H;
            // 填充条贴图第二格起点 (26,0)
            int fillX = leftPos + PRESSURE_X + PRESSURE_FILL_INNER_X;
            int fillY = topPos + PRESSURE_FILL_Y_BASE;
            Screen.blit(matrices, fillX, fillY, 63, 0, 6, fillH, 256, 256);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        super.renderLabels(matrices, mouseX, mouseY);
        assert minecraft != null;
        RenderUtils.setup(BACKGROUND);
        SCALA.draw(matrices, 8, 16);
        if (tank != null) tank.renderHighlight(matrices, mouseX, mouseY);
        if (fuel != null) fuel.renderHighlight(matrices, mouseX - this.leftPos, mouseY - this.topPos);
    }

    @Override
    protected void renderTooltip(PoseStack matrices, int mouseX, int mouseY) {
        super.renderTooltip(matrices, mouseX, mouseY);
        if (tank != null) tank.drawTooltip(matrices, mouseX, mouseY);
        if (fuel != null && blockEntity != null) {
            boolean hasTank = blockEntity.getStructure() != null && blockEntity.getStructure().hasTanks();
            fuel.addTooltip(matrices, mouseX, mouseY, hasTank);
        }
        renderPressureTooltip(matrices, mouseX, mouseY);
    }

private void renderPressureTooltip(PoseStack matrices, int mouseX, int mouseY) {
        if (blockEntity == null) return;
        int x0 = leftPos + PRESSURE_X;
        int y0 = topPos + PRESSURE_Y;
        if (mouseX >= x0 && mouseX < x0 + 63 && mouseY >= y0 && mouseY < y0 + PRESSURE_H) {
            java.util.List<Component> tooltips = new java.util.ArrayList<>();
            tooltips.add(Component.translatable("gui.cti.tooltip.pressure")
                    .append(": " + String.format("%.2f", blockEntity.getPressure()) + " bar"));
            tooltips.add(Component.translatable("gui.cti.tooltip.refinery_pressure_boost")
                    .withStyle(ChatFormatting.GRAY));
            // 温度显示（开尔文→摄氏度）
            double tempC = blockEntity.getTemperature() - 273.15;
            tooltips.add(Component.translatable("gui.cti.tooltip.soul_forge_temperature")
                    .append(": " + String.format("%.1f", tempC) + "\u00b0C")
                    .withStyle(ChatFormatting.YELLOW));
            // 当前实体熔炼倍率
            tooltips.add(Component.translatable("gui.cti.tooltip.soul_forge_multiplier")
                    .append(": x" + getMenu().getEntityMeltingMultiplier())
                    .withStyle(ChatFormatting.AQUA));
            // 温度高于 0℃ 时给出提示
            if (tempC > 0.0) {
                tooltips.add(Component.translatable("gui.cti.tooltip.soul_forge_cool_hint")
                        .withStyle(ChatFormatting.RED));
            }
            this.renderComponentTooltip(matrices, tooltips, mouseX, mouseY);
        }
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton == 0 && tank != null) {
            tank.handleClick((int) mouseX - cornerX, (int) mouseY - cornerY);
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}