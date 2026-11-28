package com.hoshino.cti.mixin.TconJEIMixin;

import com.hoshino.cti.client.TConJEITraitHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.tconjei.jei.HarvestStatsCategory;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.paypur.tconjei.ColorManager.TEXT_COLOR;

/**
 * Fixes TConJEI's harvest stats page only showing the traits of a single part type
 * (and its tooltip checking the wrong part's traits): traits are now grouped and
 * drawn separately per part type, with tooltips matched to the hovered trait.
 */
@Mixin(value = HarvestStatsCategory.class, remap = false)
public abstract class HarvestStatsCategoryMixin {

    @Overwrite
    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<HeadMaterialStats> headOptional = wrapper.getStats(HeadMaterialStats.ID);
        Optional<StatlessMaterialStats> bindingOptional = wrapper.getStats(StatlessMaterialStats.BINDING.getIdentifier());
        Optional<HandleMaterialStats> handleOptional = wrapper.getStats(HandleMaterialStats.ID);

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        headOptional.ifPresent(presentStats::add);
        bindingOptional.ifPresent(presentStats::add);
        handleOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);

        // HEAD
        if (headOptional.isPresent()) {
            HeadMaterialStats head = headOptional.get();
            TConJEITraitHelper.drawComponent(stack, head.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, head.getLocalizedInfo().get(0), lineNumber++); // durability
            TConJEITraitHelper.drawStatComponent(stack, head.getLocalizedInfo().get(1), lineNumber++); // mining tier
            TConJEITraitHelper.drawStatComponent(stack, head.getLocalizedInfo().get(2), lineNumber++); // mining speed
            TConJEITraitHelper.drawStatComponent(stack, head.getLocalizedInfo().get(3), lineNumber++); // melee damage
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // BINDING
        if (bindingOptional.isPresent()) {
            StatlessMaterialStats binding = bindingOptional.get();
            TConJEITraitHelper.drawComponent(stack, binding.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawComponent(stack, binding.getLocalizedInfo().get(0), 0, lineNumber++, TEXT_COLOR, false);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // HANDLE
        if (handleOptional.isPresent()) {
            HandleMaterialStats handle = handleOptional.get();
            TConJEITraitHelper.drawComponent(stack, handle.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, handle.getLocalizedInfo().get(0), lineNumber++); // durability
            TConJEITraitHelper.drawStatComponent(stack, handle.getLocalizedInfo().get(1), lineNumber++); // melee damage
            TConJEITraitHelper.drawStatComponent(stack, handle.getLocalizedInfo().get(2), lineNumber++); // melee speed
            TConJEITraitHelper.drawStatComponent(stack, handle.getLocalizedInfo().get(3), lineNumber);  // mining speed
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

        Optional<HeadMaterialStats> headOptional = wrapper.getStats(HeadMaterialStats.ID);
        Optional<StatlessMaterialStats> bindingOptional = wrapper.getStats(StatlessMaterialStats.BINDING.getIdentifier());
        Optional<HandleMaterialStats> handleOptional = wrapper.getStats(HandleMaterialStats.ID);

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        headOptional.ifPresent(presentStats::add);
        bindingOptional.ifPresent(presentStats::add);
        handleOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }

        // HEAD
        if (headOptional.isPresent()) {
            lineNumber++;
            HeadMaterialStats head = headOptional.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(head, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(head, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(head, 2, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(head, 3, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        // BINDING consumes a header, an info line and spacing
        if (bindingOptional.isPresent()) {
            lineNumber += TConJEITraitHelper.LINE_SPACING + 2;
        }

        // HANDLE
        if (handleOptional.isPresent()) {
            lineNumber++;
            HandleMaterialStats handle = handleOptional.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(handle, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(handle, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(handle, 2, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(handle, 3, mouseX, mouseY, lineNumber))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
        }

        return List.of();
    }
}
