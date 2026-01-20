package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class BasicOverslimeModifier extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(CtiModifiers.OVERSLIME_HANDLER.getId(),1,true));
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        GenreManager.OVERSLIME_GENRE.consumption.add(builder,5*modifier.getLevel());
    }

    @Override
    public int getPriority() {
        return 150;
    }
}
