package com.hoshino.cti.Modifier.Contributors;

import com.cazsius.solcarrot.api.SOLCarrotAPI;
import com.hoshino.cti.register.CtiEffects;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import java.util.List;

public class ChewEvolution extends BattleModifier {
    protected int getFoodLevel(Player player) {
        return SOLCarrotAPI.getFoodCapability(player).getEatenFoodCount();
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (attacker instanceof Player player) {
            int ModifierLevel = tool.getModifierLevel(this.getId());
            float per = Math.min(ModifierLevel * this.getFoodLevel(player) * 0.03F, 0.25F);
            if (this.getFoodLevel(player) >= 500 && context.getTarget() instanceof Mob mob && mob.getHealth() <= mob.getMaxHealth() * per) {
                mob.die(DamageSource.playerAttack(player));
                mob.remove(Entity.RemovalReason.KILLED);
            }
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getPlayerAttacker() != null && context.getTarget() instanceof Mob mob) {
            int level = modifier.getLevel();
            Player player = context.getPlayerAttacker();
            int count = this.getFoodLevel(player);
            if (count >= 50) {
                mob.hurt(DamageSource.indirectMagic(player, mob), count * 1.5F * level);
            }
            if (count >= 125) {
                Level MobLevel = mob.getLevel();
                double x = mob.getX();
                double y = mob.getY();
                double z = mob.getZ();
                MobLevel.explode(player, player.getX(), player.getY(), player.getZ(), 2, false, Explosion.BlockInteraction.NONE);
                List<Mob> mobbbb = MobLevel.getEntitiesOfClass(Mob.class, new AABB(x + 5, y + 5, z + 5, x - 5, y - 5, z - 5));
                for (Mob targets : mobbbb) {
                    if (targets != null) {
                        targets.hurt(DamageSource.playerAttack(player), count * 0.15f * level);
                    }
                }
            }
            if (count > 250) {
                mob.forceAddEffect(new MobEffectInstance(CtiEffects.AncientDragonFlame.get(), 60, 0), player);
            }
        }
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (target instanceof Mob mob && attacker instanceof Player player) {
            int count = this.getFoodLevel(player);
            if (count >= 50) {
                mob.hurt(DamageSource.indirectMagic(player, mob), count * 1.5F * level);
            }
            if (count >= 125) {
                Level MobLevel = mob.getLevel();
                double x = mob.getX();
                double y = mob.getY();
                double z = mob.getZ();
                MobLevel.explode(player, player.getX(), player.getY(), player.getZ(), 2, false, Explosion.BlockInteraction.NONE);
                List<Mob> mobbbb = MobLevel.getEntitiesOfClass(Mob.class, new AABB(x + 5, y + 5, z + 5, x - 5, y - 5, z - 5));
                for (Mob targets : mobbbb) {
                    if (targets != null) {
                        targets.hurt(DamageSource.playerAttack(player), count * 0.15f * level);
                    }
                }
            }
            if (count > 250) {
                mob.forceAddEffect(new MobEffectInstance(CtiEffects.AncientDragonFlame.get(), 60, 0), player);
            }
            float per = Math.min(level * this.getFoodLevel(player) * 0.03F, 0.25F);
            if (count > 500 && mob.getHealth() <= mob.getMaxHealth() * per) {
                mob.die(DamageSource.playerAttack(player));
                mob.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    @Override
    public int onDamageTool(IToolStackView tool, ModifierEntry modifier, int amount, @Nullable LivingEntity entity) {
        if (entity instanceof Player player) {
            if (this.getFoodLevel(player) >= 300) {
                return 0;
            }
        }
        return amount;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey key, TooltipFlag tooltipFlag) {
        if (player != null) {
            int count = this.getFoodLevel(player);
            int level = modifier.getLevel();
            float per = Math.min(level * count * 0.03F, 0.25F);
            list.add(Component.literal("已吃过的食物：" + count).withStyle(ChatFormatting.DARK_PURPLE));
            if (count >= 50) {
                list.add(Component.literal("每次攻击造成额外的：" + count * level * 0.15F + "魔法伤害").withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (count >= 125) {
                list.add(Component.literal("击中目标会对目标其与周围敌人造成额外的产生额外的爆炸和" + count * 0.15F * level + "的伤害").withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (count >= 250) {
                list.add(Component.literal("使攻击目标陷入“远古龙焰”状态3秒，每秒对敌方造成敌方总血量3%的真实伤害").withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (count > 300) {
                list.add(Component.literal("工具正常使用不再消耗耐久").withStyle(ChatFormatting.DARK_PURPLE));
            }
            if (count >= 500) {
                list.add(Component.literal("攻击会处决生命值低于" + per + "%的敌人").withStyle(ChatFormatting.DARK_PURPLE));
            }
        }
    }
}
