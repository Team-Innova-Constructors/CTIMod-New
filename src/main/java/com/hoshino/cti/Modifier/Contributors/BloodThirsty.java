package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import com.marth7th.solidarytinker.register.solidarytinkerEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class BloodThirsty extends BattleModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void LivingDamageEvent(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            if (ModifierUtil.getModifierLevel(stack, this.getId()) > 0) {
                int ModifierLevel = ModifierUtil.getModifierLevel(stack, this.getId());
                int time = player.getPersistentData().getInt("bloodthirsty");
                if (time < 8) {
                    player.getPersistentData().putInt("bloodthirsty", time + 1);
                } else if (time == 8) {
                    event.setAmount(event.getAmount() * 8 * ModifierLevel);
                    player.getLevel().playSound(null, player.getOnPos(), SoundEvents.ANVIL_HIT, SoundSource.PLAYERS, 1, 1);
                    player.getPersistentData().putInt("bloodthirsty", 0);
                }
            }
        }
    }

    public float DamageBoost(LivingEntity target) {
        return (target.getMaxHealth() - target.getHealth()) * 0.08F;
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (livingTarget != null && attacker instanceof Player player) {
            float amount = DamageBoost(livingTarget);
            if (player.getHealth() > 8) {
                player.setHealth(player.getHealth() - 8);
                return damage + amount;
            }
            return damage + amount;
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getAttacker() instanceof Player player && player.hasEffect(solidarytinkerEffects.bloodanger.get()) && context.getLivingTarget() != null) {
            context.getLivingTarget().hurt(DamageSource.playerAttack(player), player.getAbsorptionAmount() + player.getHealth() * 0.08F);
            player.heal(player.getMaxHealth() - player.getHealth() * 0.08F);
        }
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof Player player && player.hasEffect(solidarytinkerEffects.bloodanger.get())) {
            target.hurt(DamageSource.playerAttack(player), player.getAbsorptionAmount() + player.getHealth() * 0.08F);
            player.heal((player.getMaxHealth() - player.getHealth()) * 0.08F);
        }
    }
}
