package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Modifier.SpaceSuitModifier;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class OxidizingFlame extends BasicBurntModifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*75;
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (SpaceSuitModifier.consumeTankOxygen(player,entry,10*entry.getLevel())) {
                int effectLvl = entry.getLevel();
                var effectInstance = player.getEffect(CtiEffects.OXIDIZE_FLAME.get());
                if (effectInstance != null)
                    effectLvl += effectInstance.getAmplifier();
                else effectLvl--;
                effectLvl = Math.min(effectLvl,10*entry.getLevel()-1);
                player.addEffect(new MobEffectInstance(CtiEffects.OXIDIZE_FLAME.get(), 300, effectLvl, false, false));
            }
        }
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.burnt","info.cti.true_melee");
    }
}
