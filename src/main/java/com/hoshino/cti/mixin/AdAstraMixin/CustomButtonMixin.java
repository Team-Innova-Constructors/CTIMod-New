package com.hoshino.cti.mixin.AdAstraMixin;

import com.hoshino.cti.util.PlanetUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.ad_astra.client.screen.util.CustomButton;
import earth.terrarium.ad_astra.client.screen.util.PlanetSelectionScreen;
import earth.terrarium.ad_astra.common.data.Planet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.LinkedList;
import java.util.List;

@Mixin(remap = false, value = CustomButton.class)
public class CustomButtonMixin {
    @Final
    @Shadow
    private PlanetSelectionScreen.TooltipType tooltip;

    @Final
    @Shadow
    private Planet planetInfo;

    @Final
    @Shadow
    private Component label;

    /**
     * @author EtSH_C2H6S
     * @reason 显示环境危害
     */
    @Overwrite
    private void renderTooltips(PoseStack poseStack, int mouseX, int mouseY, Minecraft minecraft) {
        Screen screen = minecraft.screen;
        List<Component> textEntries = new LinkedList();
        String var10001;
        switch (this.tooltip) {
            case NONE:
            default:
                break;
            case GALAXY:
                var10001 = PlanetSelectionScreen.CATEGORY_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §b" + this.label.getString()));
                var10001 = PlanetSelectionScreen.TYPE_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §5" + PlanetSelectionScreen.GALAXY_TEXT.getString()));
                break;
            case SOLAR_SYSTEM:
                var10001 = PlanetSelectionScreen.CATEGORY_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §b" + this.label.getString()));
                var10001 = PlanetSelectionScreen.TYPE_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §3" + PlanetSelectionScreen.SOLAR_SYSTEM_TEXT.getString()));
                break;
            case CATEGORY:
                var10001 = PlanetSelectionScreen.CATEGORY_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §a" + this.label.getString()));
                var10001 = PlanetSelectionScreen.PROVIDED_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §b" + Component.translatable("item.ad_astra.tier_" + this.planetInfo.rocketTier() + "_rocket").getString()));
                break;
            case PLANET:
                var10001 = PlanetSelectionScreen.TYPE_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §3" + (this.planetInfo.parentWorld() == null ? PlanetSelectionScreen.PLANET_TEXT.getString() : PlanetSelectionScreen.MOON_TEXT.getString())));
                var10001 = PlanetSelectionScreen.GRAVITY_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §3" + this.planetInfo.gravity() + " m/s"));
                var10001 = PlanetSelectionScreen.OXYGEN_TEXT.getString();
                textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §" + (this.planetInfo.hasOxygen() ? "a" + PlanetSelectionScreen.OXYGEN_TRUE_TEXT.getString() : "c" + PlanetSelectionScreen.OXYGEN_FALSE_TEXT.getString())));
                String temperatureColour = "§a";
                if (this.planetInfo.temperature() > 50.0F) {
                    temperatureColour = "§6";
                } else if (this.planetInfo.temperature() < -20.0F) {
                    temperatureColour = "§1";
                }

                textEntries.add(Component.nullToEmpty("§9" + PlanetSelectionScreen.TEMPERATURE_TEXT.getString() + ": " + temperatureColour + " " + this.planetInfo.temperature() + " °C"));
                textEntries.add(Component.nullToEmpty("§b" + "压强危害" + ": " + PlanetUtil.getPressureDisplay(this.planetInfo)));
                textEntries.add(Component.nullToEmpty("§b" + "霜冻危害" + ": " + PlanetUtil.getFreezeDisplay(this.planetInfo)));
                textEntries.add(Component.nullToEmpty("§b" + "灼热危害" + ": " + PlanetUtil.getScorchDisplay(this.planetInfo)));
                textEntries.add(Component.nullToEmpty("§b" + "电离危害" + ": " + PlanetUtil.getIonizeDisplay(this.planetInfo)));
                break;
            case SPACE_STATION:
                PlanetSelectionScreen currentScreen = (PlanetSelectionScreen) minecraft.screen;
                textEntries.add(Component.nullToEmpty("§9" + PlanetSelectionScreen.ITEM_REQUIREMENT_TEXT.getString()));
                currentScreen.ingredients.forEach((ingredient) -> {
                    boolean isEnough = ((ItemStack) ingredient.getFirst()).getCount() >= (Integer) ingredient.getSecond();
                    textEntries.add(Component.nullToEmpty("§" + (isEnough ? "a" : "c") + ((ItemStack) ingredient.getFirst()).getCount() + "/" + ingredient.getSecond() + " §3" + ((ItemStack) ingredient.getFirst()).getHoverName().getString()));
                });
                textEntries.add(Component.nullToEmpty("§c----------------"));
        }

        if (this.tooltip.equals(PlanetSelectionScreen.TooltipType.ORBIT) || this.tooltip.equals(PlanetSelectionScreen.TooltipType.SPACE_STATION)) {
            var10001 = PlanetSelectionScreen.TYPE_TEXT.getString();
            textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §3" + PlanetSelectionScreen.ORBIT_TEXT.getString()));
            var10001 = PlanetSelectionScreen.GRAVITY_TEXT.getString();
            textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §3" + PlanetSelectionScreen.NO_GRAVITY_TEXT.getString()));
            var10001 = PlanetSelectionScreen.OXYGEN_TEXT.getString();
            textEntries.add(Component.nullToEmpty("§9" + var10001 + ": §c " + PlanetSelectionScreen.OXYGEN_FALSE_TEXT.getString()));
            textEntries.add(Component.nullToEmpty("§9" + PlanetSelectionScreen.TEMPERATURE_TEXT.getString() + ": §1 -270.0 °C"));
        }

        screen.renderComponentTooltip(poseStack, textEntries, mouseX, mouseY);
    }

}
