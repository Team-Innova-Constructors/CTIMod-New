package com.hoshino.cti.mixin.TconMixin;

import com.hoshino.cti.util.ICtiMeltingRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipe;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(value = MeltingRecipe.class,remap = false)
public abstract class MeltingRecipeMixin implements IMeltingRecipe, ICtiMeltingRecipe {
    @Shadow @Final protected List<FluidStack> byproducts;

    @Shadow @Nullable public abstract IMeltingContainer.OreRateType getOreType();

    @Shadow private List<List<FluidStack>> outputWithByproducts;

    @Unique public void cti$handleByproducts(IMeltingContainer inv, IFluidHandler handler, IMeltingContainer.IOreRate oreRate){
        for (FluidStack fluidStack : this.byproducts) {
            var rate = getOreType();
            if (rate!=null)
                handler.fill(new FluidStack(fluidStack.getFluid(),oreRate.applyOreBoost(getOreType(),fluidStack.getAmount())), IFluidHandler.FluidAction.EXECUTE);
            else handler.fill(fluidStack.copy(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @Unique public List<List<FluidStack>> cti$getOutputWithByproducts(IMeltingContainer.IOreRate oreRate){
        var rate = getOreType();
        if (rate!=null)
            return outputWithByproducts.stream().map(list->list.stream()
                    .map(fluidStack -> new FluidStack(fluidStack.getFluid(),oreRate.applyOreBoost(rate,fluidStack.getAmount()))).toList()).toList();
        return outputWithByproducts;
    }
}
