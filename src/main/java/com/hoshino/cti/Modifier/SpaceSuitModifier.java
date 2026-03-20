package com.hoshino.cti.Modifier;

import cofh.thermal.innovation.item.FluidReservoirItem;
import com.hoshino.cti.Modifier.Base.OxygenConsumeModifier;
import com.hoshino.cti.mixin.ThermalMixin.FluidReservoirItemMixin;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.method.FluidContainerHelper;
import mekanism.common.tags.MekanismTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
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
            if (!b1 || !(livingEntity instanceof Player player)) return;
            if (player.tickCount % 10 != 0) return;
            var fluidContainerStack = FluidContainerHelper.findFluidContainerCurio(player);
            if (fluidContainerStack == null) {
                if (hasOxygen(iToolStackView,modifierEntry) && player.getAirSupply() < player.getMaxAirSupply()) {
                    consumeOxygen(iToolStackView,modifierEntry);
                    player.setAirSupply(player.getMaxAirSupply());
                }
            } else if (tankHasOxygen(player, modifierEntry)&& player.getAirSupply() < player.getMaxAirSupply()) {
                consumeTankOxygen(player, modifierEntry);
                player.setAirSupply(player.getMaxAirSupply());
            }
        }
    }

    @Override
    public boolean hasOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank= ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        return tank.getFluid().is(MekanismTags.Fluids.OXYGEN)&&amount>1;
    }

    @Override
    public boolean tankHasOxygen(LivingEntity living, ModifierEntry modifier) {
        if (living instanceof Player player) {
            var fluidContainerStack = FluidContainerHelper.findFluidContainerCurio(player);
            if (fluidContainerStack == null) return false;
            if (fluidContainerStack.getItem() instanceof FluidReservoirItem fluidReservoirItem) {
                return fluidReservoirItem.getFluid(fluidContainerStack).getFluid().is(MekanismTags.Fluids.OXYGEN) && fluidReservoirItem.getCapacity(fluidContainerStack) > 1;
            }
        }
        return false;
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
    public void consumeTankOxygen(LivingEntity living, ModifierEntry modifier) {
        if (living instanceof Player player) {
            var fluidContainerStack = FluidContainerHelper.findFluidContainerCurio(player);
            if (fluidContainerStack == null) return;
            if (fluidContainerStack.getItem() instanceof FluidReservoirItem fluidReservoirItem) {
                FluidReservoirItemMixin container=(FluidReservoirItemMixin)fluidReservoirItem;
                container.useDrainInternal(fluidContainerStack,1, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        builder.add(new ModifierEntry(ModifierIds.tank,1));
    }
}
