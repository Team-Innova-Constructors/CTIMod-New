package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

public class BasicBurntModifier extends EtSTBaseModifier implements IModifierWithSpecialDesc {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(CtiModifiers.BURNT_HANDLER.getId(),1,true));
    }

    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0;
    }

    public float getBurntAttackSpeedBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 0;
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.burnt");
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        GenreManager.BURNT_GENRE.baseStat.add(builder,getMaxBurntBonus(context,modifier));
        GenreManager.BURNT_GENRE.mulStat.add(builder,getBurntAttackSpeedBonus(context,modifier));
    }
}
