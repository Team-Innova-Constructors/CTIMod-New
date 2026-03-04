package com.hoshino.cti.Modifier.genre.insatiable;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class NonStopInsatiable extends BasicInsatiableModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_NONSTOP_INSATIABLE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("nonstop_insatiable"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(KEY_NONSTOP_INSATIABLE,false, TinkerTags.Items.MODIFIABLE));
    }

    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 256;
    }
}
