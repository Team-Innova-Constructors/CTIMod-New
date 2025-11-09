package com.hoshino.cti.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class CtiKeyBinding {
    public static final String KEY_CATEGORY_CTI = "key.category.cti";
    public static final String KEY_STAR_HIT = "key.cti.star_hit";
    public static final String KEY_NKSSZS = "key.cti.nksszs";
    public static final KeyMapping STAR_HIT = new KeyMapping(KEY_STAR_HIT, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_CTI);
    public static final KeyMapping NKSSZS = new KeyMapping(KEY_NKSSZS, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, KEY_CATEGORY_CTI);
}
