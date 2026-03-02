package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.forTrait.OverslimeHandler;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import java.util.Arrays;
import java.util.List;

public abstract class BasicOverslimeModifier extends EtSTBaseModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(CtiModifiers.OVERSLIME_HANDLER.getId(),1,true));
    }

    public int getConsumption(IToolContext context, ModifierEntry modifier){
        return 0;
    }
    public float getDamageBase(IToolContext context, ModifierEntry modifier){
        return 0;
    }
    public float getDamageMul(IToolContext context, ModifierEntry modifier){
        return 0;
    }
    public float getArmorBase(IToolContext context, ModifierEntry modifier){
        return 0;
    }
    public float getArmorMul(IToolContext context, ModifierEntry modifier){
        return 0;
    }
    public int getOverslimeBonus(IToolContext context,ModifierEntry modifier){ return 0; }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        GenreManager.OVERSLIME_GENRE.consumption.add(builder,getConsumption(context,modifier));
        GenreManager.OVERSLIME_GENRE.baseStat.add(builder,getDamageBase(context,modifier));
        GenreManager.OVERSLIME_GENRE.mulStat.add(builder,getDamageMul(context,modifier));
        GenreManager.OVERSLIME_GENRE.baseArmorStat.add(builder,getArmorBase(context,modifier));
        GenreManager.OVERSLIME_GENRE.mulArmorStat.add(builder,getArmorMul(context,modifier));
        OverslimeModifier.OVERSLIME_STAT.add(builder,getOverslimeBonus(context,modifier));
    }

    @Override
    public List<Component> getDescriptionList() {
        if (descriptionList == null) {
            descriptionList = Arrays.asList(
                    Component.translatable(getTranslationKey() + ".flavor").withStyle(ChatFormatting.ITALIC),
                    Component.translatable(getTranslationKey() + ".description"),
                    Component.translatable("info.cti.overslime").withStyle(style -> style.withColor(0x6CA4DB)));
        }
        return descriptionList;
    }

    @Override
    public int getPriority() {
        return OverslimeHandler.OVERSLIME_MODIFIER_PRIORITY+1;
    }
}
