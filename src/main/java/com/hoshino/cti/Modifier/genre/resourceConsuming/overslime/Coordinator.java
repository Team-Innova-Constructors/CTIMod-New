package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.ArrayList;
import java.util.List;

public class Coordinator extends BasicOverslimeModifier {
    @Override
    public float getArmorMul(IToolContext context, ModifierEntry modifier) {
        return 0.05f*getArmorBonus(context.getModifiers())*modifier.getLevel();
    }
    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return Math.max(getWeaponBonus(context.getModifiers()),getArmorBonus(context.getModifiers()))*500*modifier.getLevel();
    }

    @Override
    public int getConsumption(IToolContext context, ModifierEntry modifier) {
        return Math.max(getWeaponBonus(context.getModifiers()),getArmorBonus(context.getModifiers()))*modifier.getLevel();
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.modifierAddToolStats(context, modifier, builder);
        CtiToolStats.POWER.add(builder,0.05f*modifier.getLevel()*getWeaponBonus(context.getModifiers()));
    }

    public static int getWeaponBonus(ModifierNBT modifiers){
        return modifiers.getLevel(CtiModifiers.INDUSTRIAL.getId())+modifiers.getLevel(CtiModifiers.refined.getId());
    }
    public static int getArmorBonus(ModifierNBT modifiers){
        return modifiers.getLevel(CtiModifiers.INDUSTRIAL_ARMOR.getId())+modifiers.getLevel(CtiModifiers.refined.getId());
    }

    @Override
    public List<String> getDesc() {
        var list = new ArrayList<>(super.getDesc());
        list.add("info.cti.stat_power");
        return list;
    }
}
