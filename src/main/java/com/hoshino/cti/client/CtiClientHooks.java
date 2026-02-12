package com.hoshino.cti.client;

import com.hoshino.cti.client.Screen.MorseCodeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class CtiClientHooks {
    public static void openMorseCodeScreen(BlockPos pos, int index) {
        Minecraft.getInstance().setScreen(new MorseCodeScreen(pos, index));
    }
}
