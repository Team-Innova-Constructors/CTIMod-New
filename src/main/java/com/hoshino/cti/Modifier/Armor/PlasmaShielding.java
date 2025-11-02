package com.hoshino.cti.Modifier.Armor;

import com.c2h6s.etshtinker.util.slotUtil;
import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.register.CtiToolStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class PlasmaShielding extends NoLevelsModifier implements DamageBlockModifierHook, InventoryTickModifierHook, ToolStatsModifierHook {
    public static final ResourceLocation SHIELD_LOCATION = Cti.getResource("plasma_shield");
    public static final ResourceLocation CD_LOCATION = Cti.getResource("plasma_cooldown");

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS, ModifierHooks.INVENTORY_TICK, ModifierHooks.DAMAGE_BLOCK);
    }

    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float damage) {
        if (damageSource.getEntity() instanceof Projectile projectile) {
            damageSource.setProjectile();
            projectile.discard();
            return true;
        }
        if (damage <= 0) {
            return true;
        }
        LivingEntity living = equipmentContext.getEntity();
        if (!living.level.isClientSide) {
            if (tool.getPersistentData().getInt(SHIELD_LOCATION) > 0 && equipmentContext.getEntity().invulnerableTime <= 0 && !damageSource.isProjectile() && !damageSource.isExplosion() && !damageSource.isFire()) {
                tool.getPersistentData().putInt(SHIELD_LOCATION, tool.getPersistentData().getInt(SHIELD_LOCATION) - 1);
                tool.getPersistentData().putInt(CD_LOCATION, 1200);
                living.invulnerableTime = Math.max(10, equipmentContext.getEntity().invulnerableTime);
                living.level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.METAL_HIT, living.getSoundSource(), 1, 1.5f);
                if (tool.getPersistentData().getInt(SHIELD_LOCATION) <= 0) {
                    living.level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.GLASS_BREAK, living.getSoundSource(), 1, 1.2f);
                }
                return true;
            }
        }
        return damageSource.isProjectile() || damageSource.isExplosion();
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (isCorrectSlot && !world.isClientSide) {
            if (tool.getPersistentData().getInt(SHIELD_LOCATION) < 4 && tool.getPersistentData().getInt(CD_LOCATION) <= 0) {
                tool.getPersistentData().putInt(SHIELD_LOCATION, 4);
                holder.level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, holder.getSoundSource(), 1, 0.75f);
            }
            if (tool.getPersistentData().getInt(CD_LOCATION) > 0) {
                tool.getPersistentData().putInt(CD_LOCATION, tool.getPersistentData().getInt(CD_LOCATION) - 1);
            }
        }
    }

    public static boolean isShieldActive(Player player) {
        for (EquipmentSlot slot : slotUtil.ARMOR) {
            if (player.getItemBySlot(slot).getItem() instanceof IModifiable) {
                ToolStack toolStack = ToolStack.from(player.getItemBySlot(slot));
                if (toolStack.getModifierLevel(CtiModifiers.plasma_shielding.get()) > 0) {
                    return toolStack.getPersistentData().getInt(SHIELD_LOCATION) > 0 && toolStack.getPersistentData().getInt(CD_LOCATION) <= 0;
                }
            }
        }
        return false;
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        CtiToolStats.ELECTRIC_RESISTANCE.add(modifierStatsBuilder, 1);
        CtiToolStats.FROZEN_RESISTANCE.add(modifierStatsBuilder, 1);
        CtiToolStats.SCORCH_RESISTANCE.add(modifierStatsBuilder, 1);
    }
}
