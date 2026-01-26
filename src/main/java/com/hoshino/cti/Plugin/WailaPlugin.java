package com.hoshino.cti.Plugin;

import com.hoshino.cti.Blocks.BlockEntity.AlloyCentrifugeEntity;
import com.hoshino.cti.Blocks.BlockEntity.ZirconiumCastingBlockEntity;
import me.desht.pneumaticcraft.common.thirdparty.waila.PneumaticProvider;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

@snownee.jade.api.WailaPlugin
public class WailaPlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), ZirconiumCastingBlockEntity.class);
        registration.registerBlockDataProvider(new PneumaticProvider.DataProvider(), AlloyCentrifugeEntity.class);
    }
}
