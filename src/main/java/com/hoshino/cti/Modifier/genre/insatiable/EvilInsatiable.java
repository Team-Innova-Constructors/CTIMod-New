package com.hoshino.cti.Modifier.genre.insatiable;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class EvilInsatiable extends BasicInsatiableModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_EVIL_INSATIABLE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("evil_insatiable"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ArmorLevelModule(KEY_EVIL_INSATIABLE,false, TinkerTags.Items.MODIFIABLE));
    }

    @Override
    public int getInsatiableLevel() {
        return 2;
    }

    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 16;
    }
}
