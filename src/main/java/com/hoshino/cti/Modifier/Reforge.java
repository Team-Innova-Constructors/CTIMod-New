package com.hoshino.cti.Modifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Reforge extends NoLevelsModifier implements ToolStatsModifierHook, ModifyDamageModifierHook , VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE,ModifierHooks.TOOL_STATS,ModifierHooks.VOLATILE_DATA);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        float finallyAmount=amount;
        if(source instanceof EntityDamageSource entityDamageSource&&entityDamageSource.isThorns()){
            finallyAmount=finallyAmount/0.5f;
        } else if(source.isMagic()){
            finallyAmount=finallyAmount/0.3f;
        }
        finallyAmount=finallyAmount * 0.9f;
        return finallyAmount;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.ARMOR_TOUGHNESS.add(builder,-2);
        ToolStats.ARMOR.add(builder,5);
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        volatileData.addSlots(SlotType.DEFENSE,1);
    }
}
