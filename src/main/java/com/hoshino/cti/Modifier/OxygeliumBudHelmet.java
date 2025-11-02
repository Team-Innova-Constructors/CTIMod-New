package com.hoshino.cti.Modifier;

import com.hoshino.cti.Modifier.Base.OxygenConsumeModifier;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import earth.terrarium.ad_astra.common.util.ModUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class OxygeliumBudHelmet extends OxygenConsumeModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(new ModifierTraitModule(ModifierIds.tank,1,true));
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        super.addToolStats(context, modifier, builder);
        CtiToolStats.FROZEN_RESISTANCE.add(builder,1);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(!isCorrectSlot||!(entity instanceof Player player))return;
        var tank=ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        if(tank.getFluid()== Fluids.WATER&&player.tickCount%10==0&&amount>10&&player.getAirSupply()<player.getMaxAirSupply()){
            tank.setAmount(tank.getAmount()-10);
            ToolTankHelper.TANK_HELPER.setFluid(tool,tank);
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        var entity=event.getEntity();
        if(!(entity instanceof Player player))return;
        if(event.getSource().getEntity()!=null&& GetModifierLevel.EquipHasModifierlevel(player,this.getId())&& ModUtils.isPlanet(player.level)){
            event.setAmount(event.getAmount() * 0.72f);
        }
    }

    @Override
    public boolean hasOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank=ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        return tank.getFluid()== Fluids.WATER&&amount>1;
    }

    @Override
    public void consumeOxygen(IToolStackView tool, ModifierEntry modifier) {
        var tank=ToolTankHelper.TANK_HELPER.getFluid(tool);
        int amount=tank.getAmount();
        if(tank.getFluid()== Fluids.WATER&&amount>1){
            tank.setAmount(tank.getAmount()-1);
            ToolTankHelper.TANK_HELPER.setFluid(tool,tank);
        }
    }
}
