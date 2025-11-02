package com.hoshino.cti.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ColorFulItem extends Item {
    private final int color;
    public ColorFulItem(Properties pProperties, int color) {
        super(pProperties);
        this.color=color;
    }
    @Override
    public Component getName(ItemStack pStack) {
        return Component.translatable(this.getDescriptionId(pStack)).withStyle(style -> style.withColor(color));
    }
}
