package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.RepairFactorModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class Industrial extends EtSTBaseModifier implements VolatileDataModifierHook , ToolDamageModifierHook, RepairFactorModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.VOLATILE_DATA);
        builder.addHook(this,ModifierHooks.TOOL_DAMAGE);
        builder.addHook(this,ModifierHooks.REPAIR_FACTOR);
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        volatileData.addSlots(SlotType.ABILITY,1);
        volatileData.addSlots(SlotType.UPGRADE,1);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.addToolStats(context, modifier, builder);
        ToolStats.DURABILITY.percent(builder,0.05);
        ToolStats.ATTACK_DAMAGE.percent(builder,0.05);
        ToolStats.MINING_SPEED.percent(builder,0.05);
        ToolStats.PROJECTILE_DAMAGE.percent(builder,0.05);
    }


    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity holder) {
        if (RANDOM.nextFloat()>Math.min(0.8,modifier.getLevel()*0.05)) return 0;
        return amount;
    }

    @Override
    public float getRepairFactor(IToolStackView tool, ModifierEntry entry, float factor) {
        return factor*(1+entry.getLevel()*0.05f);
    }
}
