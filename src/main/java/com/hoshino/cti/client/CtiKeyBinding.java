package com.hoshino.cti.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class CtiKeyBinding {
    public static final String KEY_CATEGORY_SOLIDARYTINKER = "key.category.cti";
    public static final String KEY_ADJUST_DIGGING_SPEED = "key.cti.star_hit";
    public static final KeyMapping STAR_HIT = new KeyMapping(KEY_ADJUST_DIGGING_SPEED, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_SOLIDARYTINKER);
}
