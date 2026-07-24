package com.hoshino.cti.mixin.Fancymenu;

import de.keksuccino.fancymenu.util.rendering.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;


@Mixin(value = GuiGraphics.class, remap = false)
public abstract class GuiGraphicsMixin {

    @Unique
    private static final List<String> cti$BLACKLISTED_SCREEN_PREFIXES = Arrays.asList(
            "de.mari_023.ae2wtlib.",
            "com.simibubi.create.",
            "appeng.client.gui.",
            "xaero."
    );

    @Shadow
    public abstract void blit(ResourceLocation texture, int x, int y, int u, int v, int width, int height);

    @Unique
    private static boolean cti$isScreenBlacklisted() {
        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen == null) {
            return false;
        }
        String screenClassName = currentScreen.getClass().getName();
        for (String prefix : cti$BLACKLISTED_SCREEN_PREFIXES) {
            if (screenClassName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }


    @Unique
    private void cti$blitVanillaStyle(ResourceLocation texture, int x, int y, int width, int height, int uWidth, int u, int v) {
        int leftWidth = width / 2;
        int rightWidth = width - leftWidth;
        this.blit(texture, x, y, u, v, leftWidth, height);
        this.blit(texture, x + leftWidth, y, u + uWidth - rightWidth, v, rightWidth, height);
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void cti$vanillaBlitOnBlacklistedScreens1(ResourceLocation texture, int x, int y, int width, int height, int sliceSize, int uWidth, int vHeight, int u, int v, CallbackInfo ci) {
        if (cti$isScreenBlacklisted()) {
            cti$blitVanillaStyle(texture, x, y, width, height, uWidth, u, v);
            ci.cancel();
        }
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void cti$vanillaBlitOnBlacklistedScreens2(ResourceLocation texture, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int u, int v, CallbackInfo ci) {
        if (cti$isScreenBlacklisted()) {
            cti$blitVanillaStyle(texture, x, y, width, height, uWidth, u, v);
            ci.cancel();
        }
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void cti$vanillaBlitOnBlacklistedScreens3(ResourceLocation texture, int x, int y, int width, int height, int leftSlice, int topSlice, int rightSlice, int bottomSlice, int uWidth, int vHeight, int u, int v, CallbackInfo ci) {
        if (cti$isScreenBlacklisted()) {
            cti$blitVanillaStyle(texture, x, y, width, height, uWidth, u, v);
            ci.cancel();
        }
    }
}
