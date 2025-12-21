package com.hoshino.cti.integration.botania.api.interfaces;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface IModifierManaLensProvider {
    @NotNull Collection<ItemStack> getLens();
}
