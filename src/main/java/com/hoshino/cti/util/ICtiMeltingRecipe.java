package com.hoshino.cti.util;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;

import java.util.List;

public interface ICtiMeltingRecipe {
     void cti$handleByproducts(IMeltingContainer inv, IFluidHandler handler, IMeltingContainer.IOreRate oreRate);
     List<List<FluidStack>> cti$getOutputWithByproducts(IMeltingContainer.IOreRate oreRate);
}
