package com.hoshino.cti.Modifier.genre.elemental;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class ElementalRelease extends Modifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_ELEMENTAL_RELEASE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("elemental_release"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_ELEMENTAL_RELEASE,false,null));
    }
}
