package com.hoshino.cti.Screen.menu;

import com.hoshino.cti.Blocks.BlockEntity.GeneralMachineEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public abstract class GeneralMachineMenu extends AbstractContainerMenu {
    public final GeneralMachineEntity entity;

    public int getEnergy() {
        return entity.getCurrentEnergy();
    }

    public FluidStack getFluidstack() {
        return entity.getFluidDis();
    }

    public GeneralMachineMenu(@Nullable MenuType<?> p_38851_, int p_38852_, GeneralMachineEntity entity) {
        super(p_38851_, p_38852_);
        this.entity = entity;
    }
}
