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
        String formattedTime = String.format("%02d:%02d", minutes, seconds);

        String curseLevelText = "诅咒等级: " + curseLevel;
        String curseTimeText = "剩余时间: " + formattedTime;

        int x = 10;
        int yBottom = screenHeight - 10;

        int curseLevelTextY = yBottom - fontRenderer.lineHeight - 2;
        poseStack.pushPose();
        fontRenderer.drawShadow(poseStack, curseLevelText, x, curseLevelTextY, 0xaa55ff);

        fontRenderer.drawShadow(poseStack, curseTimeText, x, yBottom, 0xffff7f);
        poseStack.popPose();
    });
}
