package com.hoshino.cti.Plugin;

import com.hoshino.cti.Blocks.BlockEntity.tinker.AlloyCentrifugeEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.ValveBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.ZirconiumCastingBlockEntity;
import com.hoshino.cti.Blocks.BlockEntity.tinker.refinery.RefineryControllerBlockEntity;
import me.desht.pneumaticcraft.common.thirdparty.waila.PneumaticProvider;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

@snownee.jade.api.WailaPlugin
public class WailaPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), ZirconiumCastingBlockEntity.class);
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), AlloyCentrifugeEntity.class);
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), RefineryControllerBlockEntity.class);
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), ValveBlockEntity.class);
    }
}
