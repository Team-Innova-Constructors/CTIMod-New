package com.hoshino.cti.mixin.TconJEIMixin;

import com.hoshino.cti.client.TConJEITraitHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import me.paypur.tconjei.jei.RangedStatsCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.GripMaterialStats;
import slimeknights.tconstruct.tools.stats.LimbMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.paypur.tconjei.ColorManager.TEXT_COLOR;

/**
 * Fixes TConJEI's ranged stats page only showing the traits of a single part type:
 * traits are now grouped and drawn separately per part type (limb, grip, bowstring),
 * with tooltips matched to the hovered trait.
 */
@Mixin(value = RangedStatsCategory.class, remap = false)
public abstract class RangedStatsCategoryMixin {

    @Overwrite
    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<LimbMaterialStats> limbOptional = wrapper.getStats(LimbMaterialStats.ID);
        Optional<GripMaterialStats> gripOptional = wrapper.getStats(GripMaterialStats.ID);
        Optional<StatlessMaterialStats> stringOptional = wrapper.getStats(StatlessMaterialStats.BOWSTRING.getIdentifier());

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        limbOptional.ifPresent(presentStats::add);
        gripOptional.ifPresent(presentStats::add);
        stringOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);

        // LIMB
        if (limbOptional.isPresent()) {
            LimbMaterialStats limb = limbOptional.get();
            TConJEITraitHelper.drawComponent(stack, limb.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, limb.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, limb.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, limb.getLocalizedInfo().get(2), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, limb.getLocalizedInfo().get(3), lineNumber++);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // GRIP
        if (gripOptional.isPresent()) {
            GripMaterialStats grip = gripOptional.get();
            TConJEITraitHelper.drawComponent(stack, grip.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, grip.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, grip.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, grip.getLocalizedInfo().get(2), lineNumber++);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // STRING
        if (stringOptional.isPresent()) {
            StatlessMaterialStats string = stringOptional.get();
            TConJEITraitHelper.drawComponent(stack, string.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawComponent(stack, string.getLocalizedInfo().get(0), 0, lineNumber, TEXT_COLOR, false);
        }
    }

    @Nonnull
    @Overwrite
    public List<Component> getTooltipStrings(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        // MATERIAL
        List<Component> material = TConJEITraitHelper.getMaterialFlavorTooltip(wrapper, mouseX, mouseY);
        if (!material.isEmpty()) {
            return material;
        }

        float lineNumber = 2f;

        Optional<LimbMaterialStats> limbOptional = wrapper.getStats(LimbMaterialStats.ID);
        Optional<GripMaterialStats> gripOptional = wrapper.getStats(GripMaterialStats.ID);
        Optional<StatlessMaterialStats> stringOptional = wrapper.getStats(StatlessMaterialStats.BOWSTRING.getIdentifier());

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        limbOptional.ifPresent(presentStats::add);
        gripOptional.ifPresent(presentStats::add);
        stringOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }

        // LIMB
        if (limbOptional.isPresent()) {
            lineNumber++;
            LimbMaterialStats limb = limbOptional.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(limb, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(limb, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(limb, 2, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(limb, 3, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // GRIP
        if (gripOptional.isPresent()) {
            lineNumber++;
            GripMaterialStats grip = gripOptional.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(grip, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(grip, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(grip, 2, mouseX, mouseY, lineNumber))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
        }

        return List.of();
    }
}
