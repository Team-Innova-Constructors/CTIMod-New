package com.hoshino.cti.util.method;

import cofh.core.item.FluidContainerItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class FluidContainerHelper {
    public static ItemStack findFluidContainerCurio(Player player){
        LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (handler.resolve().isPresent()) {
            for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                for (int i = 0; i < curios.getSlots(); ++i) {
                    ItemStack tank = curios.getStacks().getStackInSlot(i);
                    if (!tank.isEmpty() && tank.getItem() instanceof FluidContainerItem){
                        return tank;
                    }
                }
            }
        }
        return null;
    }
}
