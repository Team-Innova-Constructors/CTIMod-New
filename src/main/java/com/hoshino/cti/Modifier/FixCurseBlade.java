package com.hoshino.cti.Modifier;

import com.hoshino.cti.register.CtiHostilityTrait;
import com.hoshino.cti.util.EffectUtil;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;

public class FixCurseBlade extends Modifier implements MeleeHitModifierHook, ProjectileHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT, ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        var target = context.getLivingTarget();
        if (target == null) return;
        if (target instanceof Player) return;
        if (target instanceof Mob mob) {
            var cap = MobTraitCap.HOLDER.get(mob);
            if (cap.hasTrait(CtiHostilityTrait.PURIFYTRAIT.get())) return;
            EffectUtil.directAddMobEffect(target, new MobEffectInstance(LCEffects.CURSE.get(), 200, 0));
        }
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target == null) return false;
        if (target instanceof Player) return false;
        if (target instanceof Mob mob) {
            var cap = MobTraitCap.HOLDER.get(mob);
            if (cap.hasTrait(CtiHostilityTrait.PURIFYTRAIT.get())) return false;
            EffectUtil.directAddMobEffect(target, new MobEffectInstance(LCEffects.CURSE.get(), 200, 0));
        }
        return false;
    }
}
