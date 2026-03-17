package com.hoshino.cti.mixin.PowahMixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import owmii.powah.block.reactor.ReactorTile;
import owmii.powah.client.screen.Textures;
import owmii.powah.client.screen.container.ReactorScreen;
import owmii.powah.inventory.ReactorContainer;
import owmii.powah.lib.client.screen.Texture;
import owmii.powah.lib.client.screen.container.AbstractEnergyScreen;

@Mixin(value = ReactorScreen.class, remap = false)
public abstract class ReactorScreenMixin extends AbstractEnergyScreen<ReactorTile, ReactorContainer> {
    public ReactorScreenMixin(ReactorContainer container, Inventory inv, Component title, Texture backGround) {
        super(container, inv, title, backGround);
    }

    @Inject(method = "drawBackground", at = @At("TAIL"))
    protected void onDrawBackground(PoseStack matrix, float partialTicks, int mouseX, int mouseY, CallbackInfo ci) {
        double currentTemp = this.te.temp.getTicks();
        if (currentTemp < 0) {
            float coldRatio = (float) (Math.abs(currentTemp) / 100.0F);
            if (coldRatio > 1.0F) coldRatio = 1.0F;
            RenderSystem.setShaderColor(0.3F, 0.6F, 1.0F, 1.0F);
            int x = this.leftPos + 114;
            int y = this.topPos + 28;
            Textures.REACTOR_GAUGE_TEMP.drawScalableH(matrix, coldRatio, x, y);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
