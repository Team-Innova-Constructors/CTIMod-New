package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.part.MaterialItem;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import java.util.List;

import static com.hoshino.cti.util.CommonUtil.MISSING_MATERIAL_KEY;
import static com.hoshino.cti.util.CommonUtil.MISSING_STATS_KEY;
import static slimeknights.tconstruct.library.tools.part.ToolPartItem.MATERIAL_KEY;

@Mixin(value = ToolPartItem.class,remap = false)
public abstract class ToolPartItemMixin extends MaterialItem {

    @Shadow public abstract MaterialStatsId getStatType();

    public ToolPartItemMixin(Properties properties) {
        super(properties);
    }

    /**
     * @author EtSH_C2H6S
     * @reason 于1.20的匠魂部件描述同步
     */
    @Overwrite
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        if (TooltipUtil.isDisplay(stack)) {
            return;
        }
        MaterialVariantId materialVariant = this.getMaterial(stack);
        MaterialId id = materialVariant.getId();
        if (!materialVariant.equals(IMaterial.UNKNOWN_ID)) {
            // internal material ID
            if (flag.isAdvanced()) {
                tooltip.add((Component.translatable(MATERIAL_KEY, materialVariant.toString())).withStyle(ChatFormatting.DARK_GRAY));
            }
            if (this.canUseMaterial(id)) {
                // add all valid traits
                TooltipKey key = SafeClientAccess.getTooltipKey();
                for (ModifierEntry entry : MaterialRegistry.getInstance().getTraits(id, this.getStatType())) {
                    if (!entry.isBound()) {
                        continue;
                    }
                    Component name = entry.getDisplayName();
                    if (flag.isAdvanced() && Config.CLIENT.modifiersIDsInAdvancedTooltips.get()) {
                        tooltip.add(Component.translatable(TooltipUtil.KEY_ID_FORMAT, name, Component.literal(entry.getModifier().getId().toString())).withStyle(ChatFormatting.DARK_GRAY));
                    } else {
                        tooltip.add(name);
                    }
                    // holding control shows descriptions for traits
                    if (key == TooltipKey.CONTROL) {
                        // skip the flavor line, trait name will take its spot instead
                        List<Component> description = entry.getModifier().getDescriptionList(entry.getLevel());
                        for (int i = 1; i < description.size(); i++) {
                            tooltip.add(description.get(i).plainCopy().withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
                // add stats on shift
                if (key == TooltipKey.SHIFT || key == TooltipKey.UNKNOWN) {
                    MaterialRegistry.getInstance().getMaterialStats(id, this.getStatType()).ifPresent(stat -> {
                        List<Component> text = stat.getLocalizedInfo();
                        if (!text.isEmpty()) {
                            tooltip.add(Component.empty());
                            tooltip.add(stat.getLocalizedName().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE));
                            tooltip.addAll(stat.getLocalizedInfo());
                        }
                    });
                } else if (key != TooltipKey.CONTROL) {
                    // info tooltip for detailed and component info
                    tooltip.add(Component.empty());
                    tooltip.add(TooltipUtil.TOOLTIP_HOLD_SHIFT);
                    tooltip.add(TooltipUtil.TOOLTIP_HOLD_CTRL);
                }
            } else {
                // is the material missing, or is it not valid for this stat type?
                IMaterial material = MaterialRegistry.getMaterial(id);
                if (material == IMaterial.UNKNOWN) {
                    tooltip.add(Component.translatable(MISSING_MATERIAL_KEY, id));
                } else {
                    tooltip.add(Component.translatable(MISSING_STATS_KEY, this.getStatType()).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}
