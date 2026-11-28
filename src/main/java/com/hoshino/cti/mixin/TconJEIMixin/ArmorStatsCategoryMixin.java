package com.hoshino.cti.mixin.TconJEIMixin;

import com.hoshino.cti.client.TConJEITraitHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.tconjei.jei.ArmorStatsCategory;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static me.paypur.tconjei.ColorManager.*;

/**
 * Fixes TConJEI's armor stats page only showing the traits of a single part type:
 * traits are now grouped and drawn separately per part type (platings, shield core,
 * maille), with tooltips matched to the hovered trait. Also fixes the toughness and
 * knockback resistance tooltips being one line off for materials with fewer than
 * five armor slots.
 */
@Mixin(value = ArmorStatsCategory.class, remap = false)
public abstract class ArmorStatsCategoryMixin {

    @Overwrite
    public void draw(MaterialStatsWrapper wrapper, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        TConJEITraitHelper.drawMaterialHeader(wrapper, stack);

        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        float lineNumber = 2f;

        Optional<PlatingMaterialStats> helmetOptional = wrapper.getStats(PlatingMaterialStats.HELMET.getId());
        Optional<PlatingMaterialStats> chestplateOptional = wrapper.getStats(PlatingMaterialStats.CHESTPLATE.getId());
        Optional<PlatingMaterialStats> leggingsOptional = wrapper.getStats(PlatingMaterialStats.LEGGINGS.getId());
        Optional<PlatingMaterialStats> bootsOptional = wrapper.getStats(PlatingMaterialStats.BOOTS.getId());
        Optional<PlatingMaterialStats> shieldOptional = wrapper.getStats(PlatingMaterialStats.SHIELD.getId());
        Optional<StatlessMaterialStats> coreOptional = wrapper.getStats(StatlessMaterialStats.SHIELD_CORE.getIdentifier());
        Optional<StatlessMaterialStats> mailleOptional = wrapper.getStats(StatlessMaterialStats.MAILLE.getIdentifier());

        // TRAITS: one separate group per distinct part type trait list
        List<IMaterialStats> presentStats = new ArrayList<>();
        helmetOptional.ifPresent(presentStats::add);
        chestplateOptional.ifPresent(presentStats::add);
        leggingsOptional.ifPresent(presentStats::add);
        bootsOptional.ifPresent(presentStats::add);
        shieldOptional.ifPresent(presentStats::add);
        coreOptional.ifPresent(presentStats::add);
        mailleOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        TConJEITraitHelper.drawTraitLines(stack, traitLines, lineNumber);
        lineNumber++;

        List<String[]> armorStats = new ArrayList<>();
        helmetOptional.ifPresent(stats -> armorStats.add(TConJEITraitHelper.armorRow(stats)));
        chestplateOptional.ifPresent(stats -> armorStats.add(TConJEITraitHelper.armorRow(stats)));
        leggingsOptional.ifPresent(stats -> armorStats.add(TConJEITraitHelper.armorRow(stats)));
        bootsOptional.ifPresent(stats -> armorStats.add(TConJEITraitHelper.armorRow(stats)));
        shieldOptional.ifPresent(stats -> armorStats.add(TConJEITraitHelper.armorRow(stats)));

        Optional<PlatingMaterialStats> platingStats = Stream.of(helmetOptional, chestplateOptional, leggingsOptional, bootsOptional, shieldOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        if (platingStats.isPresent()) {
            PlatingMaterialStats plating = platingStats.get();
            TConJEITraitHelper.drawComponent(stack, Component.translatable("stat.tconstruct.plating").withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);

            if (plating.getType() == PlatingMaterialStats.SHIELD) {
                TConJEITraitHelper.drawStatComponent(stack, plating.getLocalizedInfo().get(0), lineNumber++); // durability
                TConJEITraitHelper.drawStatComponent(stack, plating.getLocalizedInfo().get(1), lineNumber++); // toughness
                TConJEITraitHelper.drawStatComponent(stack, plating.getLocalizedInfo().get(2), lineNumber++); // knockback resistance
            } else {
                String durabilityText = plating.getLocalizedInfo().get(0).plainCopy().getString();
                String armorText = plating.getLocalizedInfo().get(1).plainCopy().getString();

                int durabilityTextWidth = TConJEITraitHelper.width(durabilityText);
                int armorTextWidth = TConJEITraitHelper.width(armorText);

                int maxTextWidth = armorStats.stream().map(row -> TConJEITraitHelper.width(row[0])).max(Integer::compare).get();
                int maxArmorWidth = armorStats.stream().map(row -> TConJEITraitHelper.width(row[2])).max(Integer::compare).get();
                int maxDurabilityWidth = armorStats.stream().map(row -> TConJEITraitHelper.width(row[1])).max(Integer::compare).get();

                String line = "─";
                int lineWidth = TConJEITraitHelper.width(line);

                int durabilityLine = Math.max((maxTextWidth + maxArmorWidth + maxDurabilityWidth - durabilityTextWidth) / lineWidth - 1, 0);
                int armorLine = Math.max((maxTextWidth + maxArmorWidth - armorTextWidth) / lineWidth - 1, 0);

                TConJEITraitHelper.drawString(stack, durabilityText, 0, lineNumber, TEXT_COLOR, false);
                // durability line
                TConJEITraitHelper.drawString(stack, line.repeat(durabilityLine) + "┐", durabilityTextWidth, lineNumber++, DURABILITY_COLOR, true);
                TConJEITraitHelper.drawString(stack, "│", durabilityTextWidth + lineWidth * durabilityLine, lineNumber, DURABILITY_COLOR, true);

                TConJEITraitHelper.drawString(stack, armorText, 0, lineNumber, TEXT_COLOR, false);
                // armor line
                TConJEITraitHelper.drawString(stack, line.repeat(armorLine) + "┐", armorTextWidth, lineNumber++, ARMOR_COLOR, true);

                for (String[] row : armorStats) {
                    TConJEITraitHelper.drawString(stack, row[0], 0, lineNumber, TEXT_COLOR, false);
                    TConJEITraitHelper.drawString(stack, row[2], maxTextWidth + maxArmorWidth - TConJEITraitHelper.width(row[2]), lineNumber, ARMOR_COLOR, true); // armor, drawn first because its on the left
                    TConJEITraitHelper.drawString(stack, row[1], maxTextWidth + maxArmorWidth + maxDurabilityWidth - TConJEITraitHelper.width(row[1]), lineNumber++, DURABILITY_COLOR, true); // durability
                }

                // these should be the same for the whole set
                TConJEITraitHelper.drawStatComponent(stack, plating.getLocalizedInfo().get(2), lineNumber++); // toughness
                TConJEITraitHelper.drawStatComponent(stack, plating.getLocalizedInfo().get(3), lineNumber++); // knockback resistance
            }
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        if (coreOptional.isPresent()) {
            StatlessMaterialStats core = coreOptional.get();
            TConJEITraitHelper.drawComponent(stack, core.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawComponent(stack, core.getLocalizedInfo().get(0), 0, lineNumber++, TEXT_COLOR, false);
            lineNumber += TConJEITraitHelper.LINE_SPACING;
        }

        if (mailleOptional.isPresent()) {
            StatlessMaterialStats maille = mailleOptional.get();
            TConJEITraitHelper.drawComponent(stack, maille.getLocalizedName().withStyle(ChatFormatting.UNDERLINE), 0, lineNumber++, color, true);
            TConJEITraitHelper.drawComponent(stack, maille.getLocalizedInfo().get(0), 0, lineNumber, TEXT_COLOR, false);
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

        Optional<PlatingMaterialStats> helmetOptional = wrapper.getStats(PlatingMaterialStats.HELMET.getId());
        Optional<PlatingMaterialStats> chestplateOptional = wrapper.getStats(PlatingMaterialStats.CHESTPLATE.getId());
        Optional<PlatingMaterialStats> leggingsOptional = wrapper.getStats(PlatingMaterialStats.LEGGINGS.getId());
        Optional<PlatingMaterialStats> bootsOptional = wrapper.getStats(PlatingMaterialStats.BOOTS.getId());
        Optional<PlatingMaterialStats> shieldOptional = wrapper.getStats(PlatingMaterialStats.SHIELD.getId());
        Optional<StatlessMaterialStats> coreOptional = wrapper.getStats(StatlessMaterialStats.SHIELD_CORE.getIdentifier());
        Optional<StatlessMaterialStats> mailleOptional = wrapper.getStats(StatlessMaterialStats.MAILLE.getIdentifier());

        // TRAITS: check each drawn trait line so the tooltip matches the hovered trait
        List<IMaterialStats> presentStats = new ArrayList<>();
        helmetOptional.ifPresent(presentStats::add);
        chestplateOptional.ifPresent(presentStats::add);
        leggingsOptional.ifPresent(presentStats::add);
        bootsOptional.ifPresent(presentStats::add);
        shieldOptional.ifPresent(presentStats::add);
        coreOptional.ifPresent(presentStats::add);
        mailleOptional.ifPresent(presentStats::add);
        List<TConJEITraitHelper.TraitLine> traitLines = TConJEITraitHelper.buildTraitLines(wrapper, presentStats);
        List<Component> traitTooltips = TConJEITraitHelper.getTraitLineTooltips(traitLines, mouseX, mouseY, lineNumber);
        if (!traitTooltips.isEmpty()) {
            return traitTooltips;
        }
        lineNumber++;

        Optional<PlatingMaterialStats> platingStats = Stream.of(helmetOptional, chestplateOptional, leggingsOptional, bootsOptional, shieldOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();

        // PLATING
        if (platingStats.isPresent()) {
            lineNumber++;
            PlatingMaterialStats plating = platingStats.get();
            if (plating.getType() == PlatingMaterialStats.SHIELD) {
                Stream<List<Component>> stream = Stream.of(
                        TConJEITraitHelper.getStatTooltip(plating, 0, mouseX, mouseY, lineNumber++), // durability
                        TConJEITraitHelper.getStatTooltip(plating, 1, mouseX, mouseY, lineNumber++), // toughness
                        TConJEITraitHelper.getStatTooltip(plating, 2, mouseX, mouseY, lineNumber)    // knockback resistance
                );
                Optional<List<Component>> component = stream
                        .filter(list -> !list.isEmpty())
                        .findFirst();
                if (component.isPresent()) {
                    return component.get();
                }
            } else {
                // number of armor rows drawn between the armor stat and toughness
                int rows = (int) Stream.of(helmetOptional, chestplateOptional, leggingsOptional, bootsOptional, shieldOptional)
                        .filter(Optional::isPresent)
                        .count();
                Stream<List<Component>> stream = Stream.of(
                        TConJEITraitHelper.getStatTooltip(plating, 0, mouseX, mouseY, lineNumber++), // durability
                        TConJEITraitHelper.getStatTooltip(plating, 1, mouseX, mouseY, lineNumber)    // armor
                );
                lineNumber += rows + 1;
                stream = Stream.concat(stream, Stream.of(
                        TConJEITraitHelper.getStatTooltip(plating, 2, mouseX, mouseY, lineNumber++), // toughness
                        TConJEITraitHelper.getStatTooltip(plating, 3, mouseX, mouseY, lineNumber))   // knockback resistance
                );
                Optional<List<Component>> component = stream
                        .filter(list -> !list.isEmpty())
                        .findFirst();
                if (component.isPresent()) {
                    return component.get();
                }
            }
        }

        return List.of();
    }
}
