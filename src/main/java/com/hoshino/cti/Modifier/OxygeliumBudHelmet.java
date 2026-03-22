package com.hoshino.cti.Modifier;

import cofh.thermal.innovation.item.FluidReservoirItem;
import com.hoshino.cti.Modifier.Base.OxygenConsumeModifier;
import com.hoshino.cti.mixin.ThermalMixin.FluidReservoirItemMixin;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.method.FluidContainerHelper;
import com.hoshino.cti.util.method.GetModifierLevel;
import earth.terrarium.ad_astra.common.util.ModUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class OxygeliumBudHelmet extends OxygenConsumeModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(ModifierIds.tank, 1, true));
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.addToolStats(context, modifier, builder);
        CtiToolStats.FROZEN_RESISTANCE.add(builder, 1);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!isCorrectSlot || !(entity instanceof Player player)) return;
        if (player.tickCount % 10 != 0) return;
        var fluidContainerStack = FluidContainerHelper.findFluidContainerCurio(player);
        if (fluidContainerStack == null) {
            var tank = ToolTankHelper.TANK_HELPER.getFluid(tool);
            int amount = tank.getAmount();
            if (tank.getFluid() == Fluids.WATER && amount > 1 && player.getAirSupply() < player.getMaxAirSupply()) {
                tank.setAmount(tank.getAmount() - 1);
                ToolTankHelper.TANK_HELPER.setFluid(tool, tank);
                player.setAirSupply(player.getMaxAirSupply());
            }
        } else if (tankHasOxygen(player, modifier)&& player.getAirSupply() < player.getMaxAirSupply()) {
            consumeTankOxygen(player, modifier);
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        var entity = event.getEntity();
        if (!(entity instanceof Player player)) return;
        if (event.getSource().getEntity() != null && GetModifierLevel.EquipHasModifierlevel(player, this.getId()) && ModUtils.isPlanet(player.level)) {
            event.setAmount(event.getAmount() * 0.72f);
        }
    }

    @Override
    public boolean hasOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank = ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount = tank.getAmount();
        return tank.getFluid() == Fluids.WATER && amount > 1;
    }

    @Override
    public boolean tankHasOxygen(LivingEntity living, ModifierEntry modifier) {
        if (living instanceof Player player) {
            var fluidContainerStack = FluidContainerHelper.findFluidContainerCurio(player);
            if (fluidContainerStack == null) return false;
            if (fluidContainerStack.getItem() instanceof FluidReservoirItem fluidReservoirItem) {
                return fluidReservoirItem.getFluid(fluidContainerStack).getFluid() == Fluids.WATER && fluidReservoirItem.getCapacity(fluidContainerStack) > 1;
            }
        }
        return false;
    }


    @Override
    public void consumeOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank = ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount = tank.getAmount();
        if (tank.getFluid() == Fluids.WATER && amount > 1) {
            tank.setAmount(tank.getAmount() - 1);
            ToolTankHelper.TANK_HELPER.setFluid(tool, tank);
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
                player.heal(player.getMaxHealth() * 0.05f);
            }
        }
    }
}
