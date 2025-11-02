package com.hoshino.cti.Modifier.capability;

import com.hoshino.cti.Capabilitiess.IScorchShielding;
import com.hoshino.cti.Capabilitiess.ctiCapabilities;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.Supplier;

public class ScorchShieldToolCap implements IScorchShielding, ToolCapabilityProvider.IToolCapabilityProvider {
    public final Supplier<? extends IToolStackView> tool;
    public final LazyOptional<IScorchShielding> capOptional;

    public ScorchShieldToolCap(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
        tool = toolStack;
        capOptional = LazyOptional.of(() -> this);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(IToolStackView iToolStackView, @NotNull Capability<T> capability) {
        return iToolStackView.getStats().get(CtiToolStats.SCORCH_RESISTANCE) > 0 && capability == ctiCapabilities.SCORCH_SHIELDING ? ctiCapabilities.SCORCH_SHIELDING.orEmpty(capability, this.capOptional) : LazyOptional.empty();
    }

    @Override
    public float getScorchShieldinng() {
        return tool.get().getStats().get(CtiToolStats.SCORCH_RESISTANCE);
    }
}
