package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Items.SlimeCanItem;
import com.hoshino.cti.client.cache.ExposedDelay;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import dev.xkmc.l2complements.init.registrate.LCItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.mantle.client.TooltipKey;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class PlayerLocalEvent {
    @SubscribeEvent
    public static void addToolTip(ItemTooltipEvent event) {
        if (event.getItemStack().is(TIItems.STAR_ALLOY_INGOT.get())) {
            TooltipKey key = SafeClientAccess.getTooltipKey();
            if (key == TooltipKey.SHIFT) {
                List.of(
                        Component.literal("太阳的诅咒已被消除，昼夜更替已得到恢复。").withStyle(ChatFormatting.AQUA),
                        Component.literal("于失而复得的夜晚，向神明献上来自异域的供品，等待群星的回应。").withStyle(ChatFormatting.DARK_AQUA),
                        Component.translatable("etshtinker.item.tooltip.special").withStyle(ChatFormatting.LIGHT_PURPLE),
                        Component.translatable("etshtinker.item.tooltip.special2").withStyle(ChatFormatting.LIGHT_PURPLE)
                ).forEach(c -> event.getToolTip().add(c));
            } else {
                event.getToolTip().add(Component.translatable("etshtinker.item.tooltip.shift").withStyle(ChatFormatting.YELLOW));
            }
        }
        var item = event.getItemStack().getItem();
        var player = event.getEntity();
        if (player!=null){
            var osV = SlimeCanItem.getOverslimeValues(player.level);
            if (osV.containsKey(item))
                event.getToolTip().add(Component.translatable("info.cti.overslime_value").append("§a"+osV.get(item)));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addToolTipLowest(ItemTooltipEvent event) {
        if (event.getToolTip().size() > 1) {
            if (event.getItemStack().is(LCItems.SPACE_SHARD.get())) {
                event.getToolTip().set(1, Component.translatable("info.cti.space_shard").withStyle(ChatFormatting.GRAY));
            } else if (event.getItemStack().is(LCItems.EXPLOSION_SHARD.get())) {
                event.getToolTip().set(1, Component.translatable("info.cti.explosion_shard").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @SubscribeEvent
    public static void playerExposedTick(TickEvent.PlayerTickEvent event) {
        if (event.side.isServer()) return;
        if (event.phase == TickEvent.Phase.START) return;
        if (ExposedDelay.getExposedTick() > 0) {
            ExposedDelay.setExposedTick(ExposedDelay.getExposedTick() - 1);
        }
    }
}
