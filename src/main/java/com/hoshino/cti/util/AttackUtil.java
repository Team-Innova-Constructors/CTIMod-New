package com.hoshino.cti.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.definition.module.weapon.MeleeHitToolHook;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.DoubleSupplier;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.*;

public class AttackUtil {
    private static final float DEGREE_TO_RADIANS = (float)Math.PI / 180F;
    private static final AttributeModifier ANTI_KNOCKBACK_MODIFIER = new AttributeModifier(TConstruct.MOD_ID + ".anti_knockback", 1f, AttributeModifier.Operation.ADDITION);
    private static Optional<AttributeInstance> getKnockbackAttribute(@Nullable LivingEntity living) {
        return Optional.ofNullable(living)
                .map(e -> e.getAttribute(Attributes.KNOCKBACK_RESISTANCE))
                .filter(attribute -> !attribute.hasModifier(ANTI_KNOCKBACK_MODIFIER));
    }
    private static void disableKnockback(AttributeInstance instance) {
        instance.addTransientModifier(ANTI_KNOCKBACK_MODIFIER);
    }
    private static void enableKnockback(AttributeInstance instance) {
        instance.removeModifier(ANTI_KNOCKBACK_MODIFIER);
    }


    public static boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand,
                                       Entity targetEntity, DoubleSupplier cooldownFunction, boolean isExtraAttack, EquipmentSlot sourceSlot,float damagePercent) {
        if (tool.isBroken() || !tool.hasTag(TinkerTags.Items.MELEE)) {
            return false;
        }

        if (attackerLiving.level.isClientSide || !targetEntity.isAttackable() || targetEntity.skipAttackInteraction(attackerLiving)) {
            return true;
        }

        LivingEntity targetLiving = getLivingEntity(targetEntity);
        Player attackerPlayer = null;
        if (attackerLiving instanceof Player player) {
            attackerPlayer = player;
        }

        float damage = getAttributeAttackDamage(tool, attackerLiving, sourceSlot);

        float cooldown = (float)cooldownFunction.getAsDouble();
        boolean fullyCharged = cooldown > 0.9f;

        boolean isCritical = !isExtraAttack && fullyCharged && attackerLiving.fallDistance > 0.0F && !attackerLiving.isOnGround() && !attackerLiving.onClimbable()
                && !attackerLiving.isInWater() && !attackerLiving.hasEffect(MobEffects.BLINDNESS)
                && !attackerLiving.isPassenger() && targetLiving != null && !attackerLiving.isSprinting();

        ToolAttackContext context = new ToolAttackContext(attackerLiving, attackerPlayer, hand, sourceSlot, targetEntity, targetLiving, isCritical, cooldown, isExtraAttack);

        float baseDamage = damage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }

        if (damage <= 0) {
            return !isExtraAttack;
        }

        float knockback = (float)attackerLiving.getAttributeValue(Attributes.ATTACK_KNOCKBACK) / 2f;
        if (targetLiving != null) {
            knockback += 0.4f;
        }

        SoundEvent sound;
        if (attackerLiving.isSprinting() && fullyCharged) {
            sound = SoundEvents.PLAYER_ATTACK_KNOCKBACK;
            knockback += 0.5f;
        } else if (fullyCharged) {
            sound = SoundEvents.PLAYER_ATTACK_STRONG;
        } else {
            sound = SoundEvents.PLAYER_ATTACK_WEAK;
        }

        if (!isExtraAttack) {
            float criticalModifier = isCritical ? 1.5f : 1.0f;
            if (attackerPlayer != null) {
                CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer, targetEntity, isCritical, isCritical ? 1.5F : 1.0F);
                isCritical = hitResult != null;
                if (isCritical) {
                    criticalModifier = hitResult.getDamageModifier();
                }
            }
            if (isCritical) {
                damage *= criticalModifier;
            }
        }

        boolean isMagic = damage > baseDamage;
        if (cooldown < 1) {
            damage *= (0.2f + cooldown * cooldown * 0.8f);
        }
        damage*= damagePercent;

        float oldHealth = 0.0F;
        if (targetLiving != null) {
            oldHealth = targetLiving.getHealth();
        }

        float baseKnockback = knockback;
        for (ModifierEntry entry : modifiers) {
            knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);

        Optional<AttributeInstance> knockbackModifier = getKnockbackAttribute(targetLiving);
        boolean canceledKnockback = false;
        if (knockback < 0.4f) {
            canceledKnockback = true;
            knockbackModifier.ifPresent(AttackUtil::disableKnockback);
        } else if (targetLiving != null) {
            knockback -= 0.4f;
        }


        boolean didHit;
        if (isExtraAttack) {
            didHit = dealDefaultDamage(attackerLiving, targetEntity, damage);
        } else {
            didHit = MeleeHitToolHook.dealDamage(tool, context, damage);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);

        if (canceledKnockback) {
            knockbackModifier.ifPresent(AttackUtil::enableKnockback);
        }

        if (!didHit) {
            if (!isExtraAttack) {
                attackerLiving.level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);
            }
            for (ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, entry, context, damage);
            }

            return !isExtraAttack;
        }

        float damageDealt = damage;
        if (targetLiving != null) {
            damageDealt = oldHealth - targetLiving.getHealth();
        }

        if (knockback > 0) {
            if (targetLiving != null) {
                targetLiving.knockback(knockback, Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS), -Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS));
            } else {
                targetEntity.push(-Mth.sin(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback, 0.1d, Mth.cos(attackerLiving.getYRot() * DEGREE_TO_RADIANS) * knockback);
            }
            attackerLiving.setDeltaMovement(attackerLiving.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            attackerLiving.setSprinting(false);
        }

        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
            targetEntity.hurtMarked = false;
        }

        // play sound effects and particles
        if (attackerPlayer != null) {
            if (isCritical) {
                sound = SoundEvents.PLAYER_ATTACK_CRIT;
                attackerPlayer.crit(targetEntity);
            }
            if (isMagic) {
                attackerPlayer.magicCrit(targetEntity);
            }
            attackerLiving.level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), sound, attackerLiving.getSoundSource(), 1.0F, 1.0F);
        }
        if (damageDealt > 2.0F && attackerLiving.level instanceof ServerLevel server) {
            int particleCount = (int)(damageDealt * 0.5f);
            server.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(), particleCount, 0.1, 0, 0.1, 0.2);
        }

        attackerLiving.setLastHurtMob(targetEntity);
        if (targetLiving != null) {
            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
        }

        for (ModifierEntry entry : modifiers) {
            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, damageDealt);
        }

        float speed = tool.getStats().get(ToolStats.ATTACK_SPEED);
        int time = Math.round(20f / speed);
        if (time < targetEntity.invulnerableTime) {
            targetEntity.invulnerableTime = (targetEntity.invulnerableTime + time) / 2;
        }

        if (attackerPlayer != null) {
            if (targetLiving != null) {
                if (!attackerLiving.level.isClientSide && !isExtraAttack) {
                    ItemStack held = attackerLiving.getItemBySlot(sourceSlot);
                    if (!held.isEmpty()) {
                        held.hurtEnemy(targetLiving, attackerPlayer);
                    }
                }
                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
            }
            attackerPlayer.causeFoodExhaustion(0.1F);

            if (!isExtraAttack) {
                attackerPlayer.awardStat(Stats.ITEM_USED.get(tool.getItem()));
            }
        }

        if (!tool.hasTag(TinkerTags.Items.UNARMED)) {
            int durabilityLost = targetLiving != null ? 1 : 0;
            if (!tool.hasTag(TinkerTags.Items.MELEE_PRIMARY)) {
                durabilityLost *= 2;
            }
            ToolDamageUtil.damageAnimated(tool, durabilityLost, attackerLiving);
        }

        return true;
    }
}
