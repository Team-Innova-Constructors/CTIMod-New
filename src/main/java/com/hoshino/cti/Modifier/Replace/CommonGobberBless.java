package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class CommonGobberBless extends BattleModifier {

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        return damage * (1 + level * 0.15f);
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        arrow.setBaseDamage(arrow.getBaseDamage() * (1 + level * 0.1f));
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player) {
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
    public void LivingDamageEvent(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            int level = GetModifierLevel.getAllSlotModifierlevel(player, this.getId());
            if (level > 0) {
                event.setAmount(event.getAmount() * (1 - level * 0.06F));
            }
        }
    }
}
