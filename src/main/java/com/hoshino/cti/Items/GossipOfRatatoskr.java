package com.hoshino.cti.Items;

import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GossipOfRatatoskr extends CurseCurioItem {
    public GossipOfRatatoskr(Properties properties) {
        super(properties.rarity(Rarity.EPIC));
    }
    private static final String NAME = "cti:ratatoskr";
    private final int extraLevel = 100;

    @Override
    public int getExtraLevel(ItemStack stack) {
        return extraLevel;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level levelIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, levelIn, entityIn, itemSlot, isSelected);
        if (entityIn instanceof LivingEntity living) {
            int a = DifficultyLevel.ofAny(living);
            stack.getOrCreateTag().putInt(NAME, a);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        int total = stack.getOrCreateTag().getInt(NAME);
        double b = Mth.clamp(1 - (double) total / 8000, 0, 1);
        if (b != 0) {
            list.add(Component.translatable("cti.tooltip.item.gossip_of_ratatoskr.desc1").withStyle(ChatFormatting.RED));
        } else {
            list.add(Component.translatable("cti.tooltip.item.gossip_of_ratatoskr.desc2").withStyle(ChatFormatting.DARK_RED));
        }
        list.add(LangData.ITEM_CHARM_ADD_LEVEL.get(extraLevel).withStyle(ChatFormatting.RED));
    }
}
