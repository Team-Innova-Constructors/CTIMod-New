package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class CommonGobberBless extends Modifier implements MeleeDamageModifierHook, ModifyDamageModifierHook, ProjectileHitModifierHook, InventoryTickModifierHook , MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.INVENTORY_TICK,ModifierHooks.MODIFY_HURT,ModifierHooks.PROJECTILE_HIT,ModifierHooks.MELEE_HIT);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (!(projectile instanceof AbstractArrow arrow)) return false;
        var level = modifier.getLevel();
        arrow.setBaseDamage(arrow.getBaseDamage() * (1 + level * 0.1f));
        return false;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player&&isCorrectSlot) {
            for (ItemStack itemStack1 : player.getAllSlots()) {
                if (ModifierUtil.getModifierLevel(itemStack1, this.getId()) > 0 && player.tickCount % 100 == 0) {
                    float saturationLevel = player.getFoodData().getSaturationLevel();
                    int foodlevel = player.getFoodData().getFoodLevel();
                    player.getFoodData().setFoodLevel(Math.min(20, foodlevel + 1));
                    player.getFoodData().setSaturation(Math.min(20, saturationLevel + 1));
                }
            }
        }
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() instanceof WitherBoss wither && context.getAttacker() instanceof Player player) {
            wither.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic(), wither.getMaxHealth() * 0.06F);
        }
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean b) {
        var level=modifierEntry.getLevel();
        return amount * (1-(level * 0.06f));
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float baseDamage, float damage) {
        var level=modifierEntry.getLevel();
        return damage * (1 + level * 0.15f);
    }
}
