package com.hoshino.cti.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CollectionItem extends TooltipedItem{
    public final Component name;
    public CollectionItem(Properties properties, @NotNull List<Component> tooltip, Component name) {
        super(properties, tooltip);
        this.name = name;
    }
    @Override
    public @NotNull Component getName(ItemStack p_41458_) {
        return name;
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }
}
