package com.hoshino.cti.client.hud;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CurseInfoHud {
    @Getter
    @Setter
    public static int remainingCurseTime;
    @Getter
    @Setter
    public static int curseLevel;
    // 重写 EnergyHud 以显示诅咒文字
    public static final IGuiOverlay CurseHUD = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        if (minecraft.player == null || minecraft.player.isCreative()||curseLevel==0) {
            return;
        }
        int curseLevel = getCurseLevel();
        int remainingCurseTime = getRemainingCurseTime();
        int minutes = remainingCurseTime / 60;
        int seconds = remainingCurseTime % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds); // 格式化为 MM:SS

        // --- 构建要显示的文本 ---
        String curseLevelText = "诅咒等级: " + curseLevel;
        String curseTimeText = "剩余时间: " + formattedTime;

        // --- 计算左下角位置 ---
        // 距离左边缘 10 像素
        int x = 10;
        // 距离下边缘 10 像素
        int yBottom = screenHeight - 10;

        // 从底部向上绘制文字，确保它们在左下角对齐
        // 第二行（剩余时间）在底部，第一行（诅咒等级）在它上面
        int curseLevelTextY = yBottom - fontRenderer.lineHeight - 2; // 2像素作为行间距

        poseStack.pushPose(); // 保存 PoseStack 状态

        // 绘制诅咒等级文本
        // 参数：poseStack, 文本, X坐标, Y坐标, 颜色 (ARGB格式)
        fontRenderer.drawShadow(poseStack, curseLevelText, x, curseLevelTextY, 0xaa55ff); // 白色文本，带阴影

        // 绘制诅咒剩余时间文本
        fontRenderer.drawShadow(poseStack, curseTimeText, x, yBottom, 0xffff7f); // 黄色文本，带阴影

        poseStack.popPose(); // 恢复 PoseStack 状态
    });
}
