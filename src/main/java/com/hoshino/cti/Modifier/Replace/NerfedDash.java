package com.hoshino.cti.Modifier.Replace;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.mining.BreakSpeedModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class NerfedDash extends Modifier implements MeleeDamageModifierHook, BreakSpeedModifierHook, TooltipModifierHook, ConditionalStatModifierHook {
    public float getbonus(float speed, int level, int status) {
        return speed * level * status;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooks.MELEE_DAMAGE, ModifierHooks.BREAK_SPEED, ModifierHooks.CONDITIONAL_STAT);
    }

    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (context.getLivingTarget() != null && player != null) {
            float speed = (float) player.getDeltaMovement().length();
            float bonus;
            bonus = getbonus(speed, modifier.getLevel(), 2);
            return damage + bonus * baseDamage;
        }
        return damage;
    }

    @Override
    public void onBreakSpeed(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull PlayerEvent.BreakSpeed event, @Nonnull Direction sideHit, boolean isEffective, float miningSpeedModifier) {
        Player player = event.getEntity();
        if (player != null) {
            float speed = (float) player.getDeltaMovement().length();
            float bonus;
            bonus = getbonus(speed, modifier.getLevel(), 2);
            event.setNewSpeed(event.getNewSpeed() * (1 + bonus));
        }
    }

    @Override
    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity living, FloatToolStat stat, float baseValue, float multiplier) {
        int level = modifier.getLevel();
        float speed = (float) living.getDeltaMovement().length();
        float bonus;
        if (living.isSprinting()) {
            bonus = getbonus(speed, level, 2);
            if (stat == ToolStats.VELOCITY) {
                return baseValue * (1 + bonus);
            }
            return baseValue;
        }
        return baseValue;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, slimeknights.mantle.client.TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            float speed = (float) player.getDeltaMovement().length();
            float bonus = getbonus(speed, modifier.getLevel(), 2);
            boolean harvest = tool.hasTag(TinkerTags.Items.HARVEST);
            if (tooltipKey == TooltipKey.SHIFT) {
                if (harvest) {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.dash.attack_damage"), bonus, tooltip);
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.dash.draw_speed"), bonus, tooltip);
                } else {
                    TooltipModifierHook.addPercentBoost(modifier.getModifier(), Component.translatable("modifier.tinkerscalibration.dash.draw_speed"), bonus, tooltip);
                }
            }
        }
    }
}
