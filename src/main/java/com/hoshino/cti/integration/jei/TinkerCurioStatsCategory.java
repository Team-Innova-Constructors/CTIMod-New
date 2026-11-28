package com.hoshino.cti.integration.jei;

import com.hoshino.cti.Plugin.JEIPlugin;
import com.hoshino.cti.client.TConJEITraitHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.xiaoyue.tinkers_ingenuity.content.library.tools.TIMaterialStats;
import com.xiaoyue.tinkers_ingenuity.content.tools.stats.CurioMainMaterialStats;
import com.xiaoyue.tinkers_ingenuity.content.tools.stats.TIExtraMaterialStats;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import me.paypur.tconjei.jei.AbstractMaterialStatsCategory;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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

public class TinkerCurioStatsCategory extends AbstractMaterialStatsCategory {
    public TinkerCurioStatsCategory(IGuiHelper guiHelper) {
        super(guiHelper);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, TIItems.TINKER_RING.get().getRenderTool());
        this.title = Component.literal("工匠戒指");
        this.recipeType = JEIPlugin.CURIO_STATS;
        this.tag = TagKey.create(Registry.ITEM_REGISTRY,new ResourceLocation("curios:ring"));
    }

    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<CurioMainMaterialStats> ringOptional = wrapper.getStats(CurioMainMaterialStats.ID);
        Optional<TIExtraMaterialStats> extraOptional = wrapper.getStats(TIExtraMaterialStats.CURIO_EXTRA.getIdentifier());

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        ringOptional.ifPresent(presentStats::add);
        extraOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);


        if (extraOptional.isPresent()) {
            TIExtraMaterialStats extra = extraOptional.get();
            TConJEITraitHelper.drawComponent(stack, extra.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawComponent(stack, extra.getLocalizedInfo().get(0), 0, lineNumber++, TEXT_COLOR, false);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }
        if (ringOptional.isPresent()) {
            CurioMainMaterialStats ring = ringOptional.get();
            TConJEITraitHelper.drawComponent(stack, ring.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawStatComponent(stack, ring.getLocalizedInfo().get(0), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ring.getLocalizedInfo().get(1), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ring.getLocalizedInfo().get(2), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ring.getLocalizedInfo().get(3), lineNumber++);
            TConJEITraitHelper.drawStatComponent(stack, ring.getLocalizedInfo().get(4), lineNumber++);
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

        Optional<CurioMainMaterialStats> ringOptional = wrapper.getStats(CurioMainMaterialStats.ID);
        Optional<TIExtraMaterialStats> extraOptional = wrapper.getStats(TIExtraMaterialStats.CURIO_EXTRA.getIdentifier());

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        extraOptional.ifPresent(presentStats::add);
        ringOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }

        if (extraOptional.isPresent()) {
            lineNumber += TConJEITraitHelper.LINE_SPACING + 2;
        }
        if (ringOptional.isPresent()) {
            lineNumber++;
            var ring = ringOptional.get();
            Optional<List<Component>> component = Stream.of(
                            TConJEITraitHelper.getStatTooltip(ring, 0, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ring, 1, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ring, 2, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ring, 3, mouseX, mouseY, lineNumber++),
                            TConJEITraitHelper.getStatTooltip(ring, 4, mouseX, mouseY, lineNumber++))
                    .filter(list -> !list.isEmpty())
                    .findFirst();
            if (component.isPresent()) {
                return component.get();
            }
        }

        return List.of();
    }
}
