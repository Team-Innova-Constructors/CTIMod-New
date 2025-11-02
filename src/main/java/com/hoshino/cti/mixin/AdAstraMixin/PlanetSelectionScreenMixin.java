package com.hoshino.cti.mixin.AdAstraMixin;

import earth.terrarium.ad_astra.client.screen.util.PlanetSelectionScreen;
import earth.terrarium.ad_astra.common.entity.vehicle.Rocket;
import earth.terrarium.ad_astra.common.screen.menu.PlanetSelectionMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = PlanetSelectionScreen.class)
public abstract class PlanetSelectionScreenMixin extends Screen implements MenuAccess<PlanetSelectionMenu> {
    protected PlanetSelectionScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(at = @At(value = "HEAD"), method = "m_7379_")
    public void onClose(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && !(minecraft.player.getVehicle() instanceof Rocket)) {
            super.onClose();
        }
    }
}
