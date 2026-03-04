package com.hoshino.cti.Modifier.genre.insatiable;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class BasicInsatiableModifier extends EtSTBaseModifier implements IModifierWithSpecialDesc {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(new ModifierEntry(CtiModifiers.INSATIABLE_HANDLER.getId(),getInsatiableLevel()),false));
    }

    @Override
    public int getPriority() {
        return InsatiableHandler.PRIORITY+1;
    }

    public int getInsatiableLevel(){
        return 1;
    }
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier){
        return 0;
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        GenreManager.INSATIABLE_GENRE.baseStat.add(builder,getMaxInsatiableBonus(context,modifier));
    }

    @Override
    public String getDesc() {
        return "info.cti.insatiable";
    }
}
