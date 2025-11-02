package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class OverwriteKnightGlory extends BattleModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (context.getLivingTarget() != null && context.getAttacker() instanceof Player player) {
            player.setAbsorptionAmount(Mth.clamp(player.getAbsorptionAmount() + player.getMaxHealth() * 0.1F, 0, player.getMaxHealth() * (0.3F * modifier.getLevel())));
        }
    }

    @Override
    public float staticdamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity livingTarget, float baseDamage, float damage) {
        if (attacker.getAbsorptionAmount() != 0) {
            return damage + Math.min(attacker.getAbsorptionAmount() * 1F * level, attacker.getMaxHealth() * 0.3F * level);
        }
        return damage;
    }

    @Override
    public void arrowhurt(ModifierNBT modifiers, NamespacedNBT persistentData, int level, Projectile projectile, EntityHitResult hit, AbstractArrow arrow, LivingEntity attacker, LivingEntity target) {
        if (attacker instanceof Player player && target != null) {
            player.setAbsorptionAmount(Mth.clamp(player.getAbsorptionAmount() + player.getMaxHealth() * 0.1F, 0, player.getMaxHealth() * (0.3F * level)));
            arrow.setBaseDamage(arrow.getBaseDamage() + player.getAbsorptionAmount() * 0.5F * level);
        }
    }
}
