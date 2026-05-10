package com.hoshino.cti.Modifier.genre.floating;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class BasicFloatingModifier extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(new ModifierEntry(CtiModifiers.FLOATING_HANDLER.getId(),1),true));
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        GenreManager.FLOATING_GENRE.baseStat.update(builder,getBaseBonus(context,modifier));
        GenreManager.FLOATING_GENRE.mulStat.update(builder,getMulBonus(context,modifier));
    }

    public float getBaseBonus(IToolContext context,ModifierEntry modifierEntry){
        return 0;
    }
    public float getMulBonus(IToolContext context,ModifierEntry modifierEntry){
        return 0;
    }

    @Override
    public int getPriority() {
        return FloatingHandler.PRIORITY;
    }
}
