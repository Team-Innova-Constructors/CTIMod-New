package com.hoshino.cti.Modifier.capability;

import com.hoshino.cti.Capabilitiess.IFreezeShielding;
import com.hoshino.cti.Capabilitiess.ctiCapabilities;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class FreezeShieldToolCap implements IFreezeShielding, ToolCapabilityProvider.IToolCapabilityProvider {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IFreezeShielding> capOptional;

    public FreezeShieldToolCap(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
        tool = toolStack;
        capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public <T> LazyOptional<T> getCapability(IToolStackView iToolStackView, Capability<T> capability) {
        return iToolStackView.getStats().get(CtiToolStats.FROZEN_RESISTANCE) > 0 && capability == ctiCapabilities.FREEZE_SHIELDING ? ctiCapabilities.FREEZE_SHIELDING.orEmpty(capability, this.capOptional) : LazyOptional.empty();
    }

    @Override
    public float getFreezeShieldinng() {
        return tool.get().getStats().get(CtiToolStats.FROZEN_RESISTANCE);
    }
}
