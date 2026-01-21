package com.hoshino.cti.client.Screen;

import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.BreakBlockPacket;
import com.hoshino.cti.register.CtiSounds;
import com.marth7th.solidarytinker.shelf.Network.STChannel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class MorseCodeScreen extends Screen {
    private final BlockPos pos;
    private final int categoryIndex;
    private final String targetCode;
    private final String answer;
    private EditBox inputField;
    private Component statusMsg = Component.empty();

    public MorseCodeScreen(BlockPos pos, int categoryIndex) {
        super(Component.literal("解密中"));
        this.pos = pos;
        this.categoryIndex = categoryIndex;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) sb.append(random.nextInt(10));
        this.answer = sb.toString();
        this.targetCode = convertToMorse(this.answer);
    }

    private String convertToMorse(String input) {
        String[] morseTable = {
                "-----", ".----", "..---", "...--", "....-",
                ".....", "-....", "--...", "---..", "----."
        };
        StringBuilder morse = new StringBuilder();
        for (char c : input.toCharArray()) {
            morse.append(morseTable[c - '0']).append("   ");
        }
        return morse.toString();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        this.inputField = new EditBox(this.font, centerX - 80, centerY - 10, 160, 20, Component.literal("输入"));
        this.inputField.setMaxLength(7);
        this.addWidget(this.inputField);
        this.addRenderableWidget(new Button(centerX - 80, centerY + 75, 160, 20, Component.literal("确认破解"), (btn) -> {
            if (this.inputField.getValue().equals(this.answer)) {
                CtiPacketHandler.sendToServer(new BreakBlockPacket(this.pos, this.categoryIndex));
                this.onClose();
            } else {
                this.statusMsg = Component.literal("输错了,有铸币！").withStyle(ChatFormatting.RED);
                var player= Minecraft.getInstance().player;
                if(player==null)return;
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(CtiSounds.zhubi.get(),1,1));
            }
        }));
    }

    @Override
    public void render(PoseStack pose, int mouseX, int mouseY, float partial) {
        this.renderBackground(pose);
        super.render(pose, mouseX, mouseY, partial);
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        drawCenteredString(pose, this.font, this.title, centerX, 10, 0xFFFFFF);
        String[] codes = this.targetCode.trim().split("\\s+");
        float scale = 1.5f;
        pose.pushPose();
        pose.scale(scale, scale, scale);
        int scaledX = (int) (centerX / scale);
        int startY = (int) (30 / scale);
        int lineGap = 12;
        for (int i = 0; i < codes.length; i++) {
            drawCenteredString(pose, this.font, codes[i], scaledX, startY + (i * lineGap), 0xaaffff);
        }
        pose.popPose();
        this.inputField.y = centerY + 50;
        this.inputField.render(pose, mouseX, mouseY, partial);
        if (this.statusMsg != null && !this.statusMsg.getString().isEmpty()) {
            drawCenteredString(pose, this.font, this.statusMsg, centerX, this.inputField.y - 15, 0xFFFFFF);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}