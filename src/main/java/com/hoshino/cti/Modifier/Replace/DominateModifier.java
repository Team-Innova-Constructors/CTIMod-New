package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DominateModifier extends Modifier implements MeleeDamageModifierHook, ProjectileHitModifierHook {
    @Override
    public float getMeleeDamage(@Nonnull IToolStackView tool, ModifierEntry modifier, @Nonnull ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        float bonus = 0;
        if (target != null && modifier.getLevel() > 0) {
            float cap = modifier.getLevel() * 1000f;
            bonus = Math.min(cap, modifier.getLevel() >= 2 ? target.getHealth() * 0.4f : target.getHealth() * 0.25f);
        }
        return damage + bonus;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow && target != null) {
            float bonus = 0;
            if (modifier.getLevel() > 0) {
                float cap = modifier.getLevel() * 1000f;
                bonus = Math.min(cap, modifier.getLevel() >= 2 ? target.getHealth() * 0.4f : target.getHealth() * 0.25f);
            }
            arrow.setBaseDamage(arrow.getBaseDamage() + bonus);
        }
        return false;
    }

}