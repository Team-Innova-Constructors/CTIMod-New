package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import java.util.Collection;

public class FixedPurify extends Modifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_PURIFY = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("purify_level"));
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_PURIFY,false, TinkerTags.Items.MODIFIABLE));
    }
}
