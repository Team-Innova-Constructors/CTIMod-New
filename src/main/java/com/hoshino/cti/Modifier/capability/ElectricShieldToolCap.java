package com.hoshino.cti.Modifier.capability;

import com.hoshino.cti.Capabilitiess.IElectricShielding;
import com.hoshino.cti.Capabilitiess.ctiCapabilities;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class ElectricShieldToolCap implements IElectricShielding, ToolCapabilityProvider.IToolCapabilityProvider {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IElectricShielding> capOptional;

    public ElectricShieldToolCap(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
        tool = toolStack;
        capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        return iToolStackView.getStats().get(CtiToolStats.ELECTRIC_RESISTANCE) > 0 && capability == ctiCapabilities.ELECTRIC_SHIELDING ? ctiCapabilities.ELECTRIC_SHIELDING.orEmpty(capability, this.capOptional) : LazyOptional.empty();
    }

    @Override
    public float getElectricShieldinng() {
        return tool.get().getStats().get(CtiToolStats.ELECTRIC_RESISTANCE);
    }
}
