package com.hoshino.cti.api.interfaces;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.recipe.casting.ICastingRecipe;

import java.util.List;

public interface IConditionalSpeedCastingBlockEntity {
    int modifyTotalCoolingTime(FluidStack fluidStack, ICastingRecipe recipe, int initialTime);
    int getBoost(int coolingTime);
    default void onServerTickStart(){}
    default void onServerTickEnd(){}
    default void afterCraft(){}
    default List<Direction> ejectSides(){return List.of();}
}
