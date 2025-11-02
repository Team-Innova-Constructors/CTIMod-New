package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.Random;

public class Sleep {
    public Sleep() {
        MinecraftForge.EVENT_BUS.addListener(this::playertick);
    }


    private void playertick(LivingEvent.LivingTickEvent event) {
        String[] array = {
                "匠魂3的内容一直困扰着你，使你精神疲惫，再大的恐惧也无法阻止你入睡，年轻就是好，倒头就是睡",
                "看完无尽锭的合成树后，你觉得再大的恐惧也无法阻止你睡觉了",
                "你在担心前期的数值，但想起伟大的双晶态，立马倒头就睡",
                "还是睡觉吧，梦里有反物质",
                "你的精神极度压抑，需要好好钓钓鱼了,明天去钓鱼吧",
                "玩CTI玩了一天，看到后期的材料后，已经没有什么可害怕了，事已至此，先睡觉吧"

        };
        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        if (event.getEntity() instanceof Player player) {
            Random a = new Random();
            int b = a.nextInt(10);
            if (b == 2) {
                if (player.isSleeping() && SuperpositionHandler.hasCurio(player, EnigmaticItems.CURSED_RING)) {
                    if (player.getSleepTimer() == 5) {
                        if (player instanceof ServerPlayer) {
                            player.sendSystemMessage(Component.literal(array[randomIndex])
                                    .withStyle(ChatFormatting.GREEN));
                        }
                    }
                }
            }
        }
    }
}
