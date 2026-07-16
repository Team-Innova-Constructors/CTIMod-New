package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
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
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class EnderGobberBless extends Modifier implements MeleeDamageModifierHook , ModifyDamageModifierHook , ProjectileHitModifierHook , InventoryTickModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.INVENTORY_TICK,ModifierHooks.MODIFY_HURT,ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker instanceof Player) {
            var level=modifier.getLevel();
            if(!(projectile instanceof AbstractArrow arrow))return false;
            if (target instanceof EnderDragon enderDragon) {
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1 + level * 0.35f) + enderDragon.getMaxHealth() * 0.06f);
                } else {
                    arrow.setBaseDamage(arrow.getBaseDamage() * (1 + level * 0.35f));
                }

        }
        return false;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player&&isCorrectSlot) {
            if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, false, false));
            }
            for (ItemStack itemStack1 : player.getAllSlots()) {
                if (ModifierUtil.getModifierLevel(itemStack1, this.getId()) > 0 && player.tickCount % 20 == 0) {
                    float saturationLevel = player.getFoodData().getSaturationLevel();
                    int foodlevel = player.getFoodData().getFoodLevel();
                    player.getFoodData().setFoodLevel(Math.min(20, foodlevel + 1));
                    player.getFoodData().setSaturation(Math.min(20, saturationLevel + 1));
                }
            }
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext toolAttackContext, float baseDamage, float damage) {
        var attacker=toolAttackContext.getAttacker();
        var livingTarget=toolAttackContext.getLivingTarget();
        var level=modifierEntry.getLevel();
        if (attacker instanceof Player ) {
            if (livingTarget instanceof EnderDragon enderDragon) {
                return damage * (1 + level * 0.35f) + enderDragon.getMaxHealth() * 0.06F;
            }
            return damage * (1 + level * 0.35f);
        }
        return damage;
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean b) {
        var level=modifierEntry.getLevel();
        return amount * (1-(level * 0.12f));
    }
}
