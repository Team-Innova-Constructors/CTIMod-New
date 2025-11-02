package com.hoshino.cti.Modifier;

import com.hoshino.cti.Modifier.Base.OxygenConsumeModifier;
import com.hoshino.cti.register.CtiToolStats;
import mekanism.common.registries.MekanismFluids;
import mekanism.common.tags.MekanismTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.data.ModifierIds;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public class SpaceSuitModifier extends OxygenConsumeModifier implements ToolStatsModifierHook, InventoryTickModifierHook , ModifierTraitHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.INVENTORY_TICK,ModifierHooks.MODIFIER_TRAITS);
    }

    @Override
    public void addToolStats(IToolContext tool, ModifierEntry modifier, ModifierStatsBuilder builder) {
        CtiToolStats.SCORCH_RESISTANCE.add(builder, 0.25 * modifier.getLevel());
        CtiToolStats.FROZEN_RESISTANCE.add(builder, 0.25 * modifier.getLevel());
        CtiToolStats.ELECTRIC_RESISTANCE.add(builder, 0.1 * modifier.getLevel());
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, @NotNull ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if (iToolStackView.hasTag(TinkerTags.Items.HELMETS)&&livingEntity.isInWater() &&level.getGameTime()%10==0&&hasOxygen(iToolStackView,modifierEntry)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 12, 0, false, false));
            consumeOxygen(iToolStackView,modifierEntry);
        }
    }

    @Override
    public boolean hasOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank= ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        return tank.getFluid().is(MekanismTags.Fluids.OXYGEN)&&amount>1;
    }

    @Override
    public void consumeOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank=ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        if(tank.getFluid().is(MekanismTags.Fluids.OXYGEN)&&amount>1){
            tank.setAmount(tank.getAmount()-1);
            ToolTankHelper.TANK_HELPER.setFluid(tool,tank);
        }
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        builder.add(new ModifierEntry(ModifierIds.tank,1));
    }
}
