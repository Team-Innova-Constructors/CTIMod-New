package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Cti;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BasicOverHeatModifier extends BasicBurntModifier implements LeftClickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK);
    }

    public static final ResourceLocation KEY_HEAT = Cti.getResource("heat_sword");

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (tool.getPersistentData().getInt(KEY_HEAT)>=100){
                player.addEffect(new MobEffectInstance(CtiEffects.OVERHEAT.get(),200,0,false,false));
                tool.getPersistentData().remove(KEY_HEAT);
            }
        }
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (tool.getPersistentData().getInt(KEY_HEAT)>=100){
                player.addEffect(new MobEffectInstance(CtiEffects.OVERHEAT.get(),200,0,false,false));
                tool.getPersistentData().remove(KEY_HEAT);
            }
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide){
            if (tool.getPersistentData().getInt(KEY_HEAT)>=100){
                player.addEffect(new MobEffectInstance(CtiEffects.OVERHEAT.get(),200,0,false,false));
                tool.getPersistentData().remove(KEY_HEAT);
            }
        }
    }
}
