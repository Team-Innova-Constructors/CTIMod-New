package com.hoshino.cti.mixin.ThermalMixin;

import cofh.thermal.innovation.item.FluidReservoirItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FluidReservoirItem.class)
public interface FluidReservoirItemMixin {
    @Invoker(value = "drainInternal",remap = false)
     FluidStack useDrainInternal(ItemStack container, int maxDrain, IFluidHandler.FluidAction action);
}
