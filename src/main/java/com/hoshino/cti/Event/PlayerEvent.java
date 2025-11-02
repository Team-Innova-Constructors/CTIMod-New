package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import dev.xkmc.l2complements.init.registrate.LCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.part.ToolPartItem;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class PlayerEvent {
    @SubscribeEvent
    public static void addToolTip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof ToolPartItem partItem) {
            TooltipKey key = SafeClientAccess.getTooltipKey();
            if (key == TooltipKey.CONTROL) {
                MaterialVariantId materialVariant = partItem.getMaterial(event.getItemStack());
                MaterialId id = materialVariant.getId();
                if (partItem.canUseMaterial(id)) {
                    event.getToolTip().add(Component.literal("材料特性描述："));
                    for (ModifierEntry entry : MaterialRegistry.getInstance().getTraits(id, partItem.getStatType())) {
                        event.getToolTip().add(entry.getModifier().getDisplayName());
                        List<Component> description = entry.getModifier().getDescriptionList(entry.getLevel());
                        for (int i = 1; i < description.size(); i++) {
                            event.getToolTip().add(description.get(i).plainCopy().withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
            } else event.getToolTip().add(Component.literal("按住§bCtrl§r查看材料特性描述"));
        }
        else if (event.getItemStack().is(TIItems.STAR_ALLOY_INGOT.get())){
            TooltipKey key = SafeClientAccess.getTooltipKey();
            if (key==TooltipKey.SHIFT){
                List.of(
                        Component.literal("太阳的诅咒已被消除，昼夜更替已得到恢复。").withStyle(ChatFormatting.AQUA),
                        Component.literal("于失而复得的夜晚，向神明献上来自异域的供品，等待群星的回应。").withStyle(ChatFormatting.DARK_AQUA),
                        Component.translatable("etshtinker.item.tooltip.special").withStyle(ChatFormatting.LIGHT_PURPLE),
                        Component.translatable("etshtinker.item.tooltip.special2").withStyle(ChatFormatting.LIGHT_PURPLE)
                ).forEach(c->event.getToolTip().add(c));
            } else {
                event.getToolTip().add(Component.translatable("etshtinker.item.tooltip.shift").withStyle(ChatFormatting.YELLOW));
            }
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addToolTipLowest(ItemTooltipEvent event) {
        if (event.getToolTip().size()>1) {
            if (event.getItemStack().is(LCItems.SPACE_SHARD.get())) {
                event.getToolTip().set(1, Component.translatable("info.cti.space_shard").withStyle(ChatFormatting.GRAY));
            } else if (event.getItemStack().is(LCItems.EXPLOSION_SHARD.get())) {
                event.getToolTip().set(1, Component.translatable("info.cti.explosion_shard").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
