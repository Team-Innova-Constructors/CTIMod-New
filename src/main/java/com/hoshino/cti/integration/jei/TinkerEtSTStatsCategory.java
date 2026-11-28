package com.hoshino.cti.integration.jei;

import com.c2h6s.etshtinker.init.ItemReg.etshtinkerItems;
import com.c2h6s.etshtinker.tools.stats.PlasmaGeneratorMaterialStats;
import com.c2h6s.etshtinker.tools.stats.ionizerMaterialStats;
import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.client.TConJEITraitHelper;
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

public class TinkerEtSTStatsCategory extends AbstractMaterialStatsCategory {
    public TinkerEtSTStatsCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, etshtinkerItems.constrained_plasma_saber.get().getRenderTool());
        this.title = Component.literal("离子化射线炮/约束等离子阔剑");
        this.recipeType = JEIPlugin.ETST_STATS;
        this.tag = TinkerTags.Items.MELEE;
    }

    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<PlasmaGeneratorMaterialStats> plasmaGeneratorMaterialStats = wrapper.getStats(PlasmaGeneratorMaterialStats.ID);
        Optional<ionizerMaterialStats> ionizerStats = wrapper.getStats(ionizerMaterialStats.ID);

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        plasmaGeneratorMaterialStats.ifPresent(presentStats::add);
        ionizerStats.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);


        if (ionizerStats.isPresent()) {
            var ionizer = ionizerStats.get();
            TConJEITraitHelper.drawComponent(stack, ionizer.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, ionizer.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ionizer.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ionizer.getLocalizedInfo().get(2), lineNumber++);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }
        if (plasmaGeneratorMaterialStats.isPresent()) {
            var generator = plasmaGeneratorMaterialStats.get();
            TConJEITraitHelper.drawComponent(stack, generator.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, generator.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, generator.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, generator.getLocalizedInfo().get(2), lineNumber++);
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

        Optional<PlasmaGeneratorMaterialStats> plasmaGeneratorMaterialStats = wrapper.getStats(PlasmaGeneratorMaterialStats.ID);
        Optional<ionizerMaterialStats> ionizerStats = wrapper.getStats(ionizerMaterialStats.ID);

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        plasmaGeneratorMaterialStats.ifPresent(presentStats::add);
        ionizerStats.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }

        if (ionizerStats.isPresent()) {
            lineNumber++;
            var ionizer = ionizerStats.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(ionizer, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ionizer, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ionizer, 2, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }
        if (plasmaGeneratorMaterialStats.isPresent()) {
            lineNumber++;
            var generator = plasmaGeneratorMaterialStats.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(generator, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(generator, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(generator, 2, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
        }

        return List.of();
    }
}
