package com.hoshino.cti.integration.jei;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.client.TConJEITraitHelper;
import com.marth7th.solidarytinker.register.SolidarytinkerItem;
import com.marth7th.solidarytinker.tools.Stats.PerceptionCoreStats;
import com.marth7th.solidarytinker.tools.Stats.SoulGeHeartMaterialStats;
import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.tconjei.jei.AbstractMaterialStatsCategory;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TinkerSTStatsCategory extends AbstractMaterialStatsCategory {
    public TinkerSTStatsCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SolidarytinkerItem.soulge.get().getRenderTool());
        this.title = Component.literal("魂戈/感知刺剑");
        this.recipeType = JEIPlugin.ST_STATS;
        this.tag = TinkerTags.Items.MELEE;
    }

    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<SoulGeHeartMaterialStats> soulGeHeartStats = wrapper.getStats(SoulGeHeartMaterialStats.ID);
        Optional<PerceptionCoreStats> perceptionCoreStats = wrapper.getStats(PerceptionCoreStats.ID);

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        soulGeHeartStats.ifPresent(presentStats::add);
        perceptionCoreStats.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);


        if (perceptionCoreStats.isPresent()) {
            var core = perceptionCoreStats.get();
            TConJEITraitHelper.drawComponent(stack, core.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, core.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, core.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, core.getLocalizedInfo().get(2), lineNumber++);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }
        if (soulGeHeartStats.isPresent()) {
            var heart = soulGeHeartStats.get();
            TConJEITraitHelper.drawComponent(stack, heart.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, heart.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, heart.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, heart.getLocalizedInfo().get(2), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, heart.getLocalizedInfo().get(3), lineNumber++);
        }
    }

    @Nonnull
    public List<Component> getTooltipStrings(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        // MATERIAL
        List<Component> material = TConJEITraitHelper.getMaterialFlavorTooltip(wrapper, mouseX, mouseY);
        if (!material.isEmpty()) {
            return material;
        }

        float lineNumber = 2f;

        Optional<SoulGeHeartMaterialStats> soulGeHeartStats = wrapper.getStats(SoulGeHeartMaterialStats.ID);
        Optional<PerceptionCoreStats> perceptionCoreStats = wrapper.getStats(PerceptionCoreStats.ID);

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        soulGeHeartStats.ifPresent(presentStats::add);
        perceptionCoreStats.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }

        if (perceptionCoreStats.isPresent()) {
            lineNumber++;
            var core = perceptionCoreStats.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(core, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(core, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(core, 2, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }
        if (soulGeHeartStats.isPresent()) {
            lineNumber++;
            var heart = soulGeHeartStats.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(heart, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(heart, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(heart, 2, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(heart, 3, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
        }

        return List.of();
    }
}
