package com.hoshino.cti.Modifier.capability;

import com.hoshino.cti.Capabilitiess.IPressureShielding;
import com.hoshino.cti.Capabilitiess.ctiCapabilities;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class PressureShieldToolCap implements IPressureShielding, ToolCapabilityProvider.IToolCapabilityProvider {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IPressureShielding> capOptional;

    public PressureShieldToolCap(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
        tool = toolStack;
        capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        return iToolStackView.getStats().get(CtiToolStats.PRESSURE_RESISTANCE) > 0 && capability == ctiCapabilities.PRESSURE_SHIELDING ? ctiCapabilities.PRESSURE_SHIELDING.orEmpty(capability, this.capOptional) : LazyOptional.empty();
    }

    @Override
    public float getPressureShielding() {
        return tool.get().getStats().get(CtiToolStats.PRESSURE_RESISTANCE);
    }
}
