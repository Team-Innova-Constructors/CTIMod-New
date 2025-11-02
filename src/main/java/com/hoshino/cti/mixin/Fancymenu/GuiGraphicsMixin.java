package com.hoshino.cti.mixin.Fancymenu;

import de.keksuccino.fancymenu.util.rendering.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;


@OnlyIn(Dist.CLIENT)
@Mixin(value = GuiGraphics.class, remap = false)
public abstract class GuiGraphicsMixin {

    private static final List<String> BLACKLISTED_SCREEN_PREFIXES = Arrays.asList(
            "de.mari_023.ae2wtlib.",
            "com.simibubi.create.",
            "appeng.client.gui."
    );

    private boolean isScreenBlacklisted() {

        Screen currentScreen = Minecraft.getInstance().screen;
        if (currentScreen == null) {
            return false;
        }
        String screenClassName = currentScreen.getClass().getName();
        for (String prefix : BLACKLISTED_SCREEN_PREFIXES) {
            if (screenClassName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onBlitNineSliced1(ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p, int q, CallbackInfo ci) {
        if (isScreenBlacklisted()) {
            ci.cancel();
        }
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onBlitNineSliced2(ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, CallbackInfo ci) {
        if (isScreenBlacklisted()) {
            ci.cancel();
        }
    }

    @Inject(method = "blitNineSliced(Lnet/minecraft/resources/ResourceLocation;IIIIIIIIIIII)V",
            at = @At("HEAD"),
            cancellable = true)
    private void onBlitNineSliced3(ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, int o, int p, int q, int r, int s, int t, CallbackInfo ci) {
        if (isScreenBlacklisted()) {
            ci.cancel();
        }
    }
}

