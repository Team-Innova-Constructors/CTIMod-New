package com.hoshino.cti.Modifier.genre.insatiable;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class ReplacedWellTrained extends Modifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_WELL_TRAINED = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("well_trained"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_WELL_TRAINED,false, TinkerTags.Items.MODIFIABLE));
    }
}
