package com.hoshino.cti.Modifier.aetherCompact;

import com.aetherteam.aether.data.resources.AetherDamageTypes;
import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.Entities.plasmaexplosionentity;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.hooks.AfterPlasmaSlashHitModifierHook;
import com.c2h6s.etshtinker.hooks.BeforePlasmaSlashHitModifierHook;
import com.c2h6s.etshtinker.hooks.PlasmaExplosionHitModifierHook;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class ValkyrieBless extends EtSTBaseModifier implements PlasmaExplosionHitModifierHook, AfterPlasmaSlashHitModifierHook, BeforePlasmaSlashHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,etshtinkerHook.AFTER_SLASH_HIT,etshtinkerHook.PLASMA_EXPLOSION_HIT,etshtinkerHook.BEFORE_SLASH_HIT);
    }

    public static boolean b1 = false;

    @Override
    public void beforePlasmaSlashHit(ToolStack tool, LivingEntity target, PlasmaSlashEntity slash, boolean isCritical) {
        b1 = true;
    }

    @Override
    public void afterPlasmaSlashHit(ToolStack tool, LivingEntity target, PlasmaSlashEntity slash, boolean isCritical, float slashDamage) {
        b1 = false;
    }
    @Override
    public void beforePlasmaExplosionHit(ToolStack tool, LivingEntity target, plasmaexplosionentity explosion, boolean isCritical) {
        b1 = true;
    }

    @Override
    public void afterPlasmaExplosionHit(ToolStack tool, LivingEntity target, plasmaexplosionentity explosion, boolean isCritical) {
        b1 = false;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity) {
            Entity entity = context.getTarget();
            entity.invulnerableTime = 0;
            entity.hurt(RANDOM.nextBoolean() ? AetherDamageTypes.cloudCrystal(context.getAttacker(), context.getAttacker()) : AetherDamageTypes.thunderCrystal(context.getAttacker(), context.getAttacker()), damage * 0.2f * modifier.getLevel());
            entity.invulnerableTime = 0;
            if (!context.isExtraAttack()||(b1&&RANDOM.nextFloat()<=0.3)) {
                LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, entity.level);
                bolt.setPos(entity.position());
                bolt.setCause(context.getAttacker() instanceof ServerPlayer player ? player : null);
                bolt.setDamage(damage * 0.3f);
                bolt.addTag("valkyrie");
                entity.level.addFreshEntity(bolt);
                b1 =false;
            } else {
                DamageSource source = new EntityDamageSource(DamageSource.LIGHTNING_BOLT.msgId,context.getAttacker()).bypassArmor();
                entity.invulnerableTime = 0;
                entity.hurt(source, damage * 0.5f * modifier.getLevel());
                entity.invulnerableTime = 0;
            }

        }
    }

    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (target!=null&&attacker!=null&&projectile instanceof AbstractArrow arrow) {
            target.invulnerableTime = 0;
            target.hurt(RANDOM.nextBoolean() ? AetherDamageTypes.cloudCrystal(attacker, attacker) : AetherDamageTypes.thunderCrystal(attacker, attacker), (float) (arrow.getBaseDamage()*arrow.getDeltaMovement().length() * 0.2f * modifier.getLevel()));
            target.invulnerableTime = 0;
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, target.level);
            bolt.setPos(target.position());
            bolt.setCause(attacker instanceof ServerPlayer player ? player : null);
            bolt.setDamage((float) (arrow.getBaseDamage() * 0.3f));
            bolt.addTag("valkyrie");
            target.level.addFreshEntity(bolt);
        }
        return false;
    }
}
