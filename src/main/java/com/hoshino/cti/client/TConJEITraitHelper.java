package com.hoshino.cti.client;

import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.mojang.blaze3d.vertex.PoseStack;
import me.paypur.tconjei.ColorManager;
import me.paypur.tconjei.Utils;
import me.paypur.tconjei.jei.MaterialStatsWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeI18n;
import slimeknights.mantle.client.ResourceColorManager;
import slimeknights.tconstruct.library.client.materials.MaterialTooltipCache;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared helpers for the TConJEI material stats category mixins.
 * <p>
 * Fixes TConJEI only showing one part type's trait list: trait lists that differ
 * between part types (e.g. head vs. handle) are displayed as separate groups.
 * Each group's part type label takes its own line with the trait list starting
 * on the next line; a single group covering all parts is drawn unlabeled.
 * Tooltips are matched to the exact trait under the cursor.
 */
@OnlyIn(Dist.CLIENT)
public final class TConJEITraitHelper {

    public static final int LINE_HEIGHT = 10;
    public static final float LINE_SPACING = 0.5f;
    public static final int WIDTH = 178;

    private TConJEITraitHelper() {}

    private static Font font() {
        return Minecraft.getInstance().font;
    }

    public static int width(Component component) {
        return font().width(component);
    }

    public static int width(String text) {
        return font().width(text);
    }

    /** One drawn line of the trait area: a part type label line (trait null) or a trait name line (label null). */
    public record TraitLine(String label, ModifierEntry trait) {}

    /**
     * Builds the trait lines for the given part types, creating one separate group
     * per distinct trait list. Identical lists are merged into a single group
     * labeled with every part type sharing them; if all parts share the same list,
     * the group is drawn without a label, matching the original single-list layout.
     */
    public static List<TraitLine> buildTraitLines(MaterialStatsWrapper wrapper, List<IMaterialStats> presentStats) {
        List<List<ModifierEntry>> traitGroups = new ArrayList<>();
        List<List<String>> groupParts = new ArrayList<>();
        for (IMaterialStats stats : presentStats) {
            List<ModifierEntry> traits = wrapper.getTraits(stats.getIdentifier());
            if (traits.isEmpty()) {
                continue;
            }
            String partName = stats.getLocalizedName().getString().split(" ")[0];
            int group = -1;
            for (int i = 0; i < traitGroups.size(); i++) {
                if (sameTraits(traitGroups.get(i), traits)) {
                    group = i;
                    break;
                }
            }
            if (group < 0) {
                traitGroups.add(traits);
                groupParts.add(new ArrayList<>(List.of(partName)));
            } else if (!groupParts.get(group).contains(partName)) {
                groupParts.get(group).add(partName);
            }
        }
        boolean singleGroup = traitGroups.size() == 1;
        List<TraitLine> lines = new ArrayList<>();
        for (int i = 0; i < traitGroups.size(); i++) {
            if (!singleGroup) {
                // part type label gets its own line above the trait list
                lines.add(new TraitLine(String.join(", ", groupParts.get(i)) + ": ", null));
            }
            for (ModifierEntry trait : traitGroups.get(i)) {
                lines.add(new TraitLine(null, trait));
            }
        }
        return lines;
    }

    /**
     * Draws the trait lines right aligned at the given line, returning the number of lines consumed.
     * Part type label lines take one line, the trait list starts on the next line.
     */
    public static int drawTraitLines(PoseStack stack, List<TraitLine> lines, float lineNumber) {
        Font font = font();
        for (TraitLine line : lines) {
            if (line.trait() == null) {
                // part type label on its own line, right aligned
                String label = line.label();
                var component = Component.literal(label).withStyle(style -> style.withColor(ColorManager.TEXT_COLOR).withBold(true).withUnderlined(true));
                drawComponent(stack, component, WIDTH - font.width(label), lineNumber, ColorManager.TEXT_COLOR, false);
            } else {
                ModifierEntry trait = line.trait();
                Component name = trait.getDisplayName().copy().withStyle(style -> style.withColor((TextColor) null));
                int nameColor = ResourceColorManager.getColor(Util.makeTranslationKey("modifier", trait.getId()));
                int nameWidth = font.width(name);
                drawComponent(stack, name, WIDTH - nameWidth, lineNumber, nameColor, true);
            }
            lineNumber++;
        }
        return lines.size();
    }

    /**
     * Returns the tooltip of the trait line under the cursor, or an empty list.
     * Label lines carry no tooltip.
     */
    public static List<Component> getTraitLineTooltips(List<TraitLine> lines, double mouseX, double mouseY, float lineNumber) {
        Font font = font();
        for (TraitLine line : lines) {
            if (line.trait() != null) {
                int width = font.width(line.trait().getDisplayName());
                if (Utils.inBox(mouseX, mouseY, WIDTH - width, lineNumber * LINE_HEIGHT - 1, width, LINE_HEIGHT)) {
                    String key = Util.makeTranslationKey("modifier", line.trait().getId());
                    var list = new ArrayList<>(List.of((Component)Component.translatable(key + ".flavor").withStyle(ChatFormatting.ITALIC),
                            Component.translatable(key + ".description")));
                    if (line.trait().isBound())
                        list.add(Component.translatable("cti.tooltip.modifier.piority").append("§b" + line.trait().getModifier().getPriority()));
                    if (line.trait().getModifier() instanceof IModifierWithSpecialDesc specialDesc)
                        specialDesc.getDesc().forEach(s -> list.add(Component.translatable(s)));
                    return list;
                }
            }
            lineNumber++;
        }
        return List.of();
    }

    /**
     * Draws the centered material name (line 0) and tier (line 1).
     */
    public static void drawMaterialHeader(MaterialStatsWrapper wrapper, PoseStack stack) {
        final int tier = wrapper.material().getTier();
        final int color = MaterialTooltipCache.getColor(wrapper.getMaterialId()).getValue();
        drawComponentShadowCentered(stack, Component.translatable(Util.makeTranslationKey("material", wrapper.getMaterialId())).withStyle(ChatFormatting.UNDERLINE), 0, color);
        drawComponentShadowCentered(stack, Component.translatable("tconjei.tooltip.tier", tier), 1, ColorManager.getTierColor(tier).orElse(color));
    }

    /**
     * Tooltip of the material flavor text shown when hovering the material name.
     */
    public static List<Component> getMaterialFlavorTooltip(MaterialStatsWrapper wrapper, double mouseX, double mouseY) {
        final String key = Util.makeTranslationKey("material", wrapper.getMaterialId());
        final int width = font().width(ForgeI18n.getPattern(key));
        if (Utils.inBox(mouseX, mouseY, (WIDTH - width) / 2f, -1, width, LINE_HEIGHT)) {
            return List.of(Component.translatable(key + ".flavor").withStyle(ChatFormatting.ITALIC));
        }
        return List.of();
    }

    public static void drawComponent(PoseStack stack, Component component, int x, float lineNumber, int color, boolean shadow) {
        final Font font = font();
        final float y = lineNumber * LINE_HEIGHT;
        if (shadow) {
            font.draw(stack, component, (float) x + 1, y + 1, ColorManager.getShade(color, 6));
        }
        font.draw(stack, component, (float) x, y, color);
    }

    public static void drawString(PoseStack stack, String string, int x, float lineNumber, int color, boolean shadow) {
        final Font font = font();
        final float y = lineNumber * LINE_HEIGHT;
        if (shadow) {
            font.draw(stack, string, (float) x + 1, y + 1, ColorManager.getShade(color, 6));
        }
        font.draw(stack, string, (float) x, y, color);
    }

    /* has shadow by default */
    public static void drawStatComponent(PoseStack stack, Component component, float lineNumber) {
        final Font font = font();
        Component sibling = component.getSiblings().get(0);
        drawComponent(stack, sibling.plainCopy(), font.width(component.plainCopy()), lineNumber, sibling.getStyle().getColor().getValue(), true);
        drawComponent(stack, component.plainCopy(), 0, lineNumber, ColorManager.TEXT_COLOR, false);
    }

    public static void drawComponentShadowCentered(PoseStack stack, Component component, float lineNumber, int color) {
        drawComponent(stack, component, (WIDTH - font().width(component)) / 2, lineNumber, color, true);
    }

    public static List<Component> getStatTooltip(IMaterialStats stats, int i, double mouseX, double mouseY, float lineNumber) {
        final int width = font().width(stats.getLocalizedInfo().get(i).plainCopy());
        if (Utils.inBox(mouseX, mouseY, 0, lineNumber * LINE_HEIGHT - 1, width, LINE_HEIGHT)) {
            return List.of(stats.getLocalizedDescriptions().get(i));
        }
        return List.of();
    }

    /**
     * Row of the armor plating table: { part name, durability, armor }.
     */
    public static String[] armorRow(PlatingMaterialStats stats) {
        return new String[]{stats.getLocalizedName().getString().split(" ")[0] + ": ", String.valueOf(stats.durability()), stats.armor() + " "};
    }

    private static boolean sameTraits(List<ModifierEntry> first, List<ModifierEntry> second) {
        if (first.size() != second.size()) {
            return false;
        }
        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).getId().equals(second.get(i).getId()) || first.get(i).getLevel() != second.get(i).getLevel()) {
                return false;
            }
        }
        return true;
    }
}
