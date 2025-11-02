package com.hoshino.cti.Modifier.iafCompact;

import com.c2h6s.etshtinker.Entities.plasmaexplosionentity;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.hooks.PlasmaExplosionHitModifierHook;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.c2h6s.etshtinker.util.vecCalc;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.Entity.Projectiles.PlasmaWaveSlashProjectile;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PlasmaWaveSlashEx extends EtSTBaseModifier implements LeftClickModifierHook, PlasmaExplosionHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, CtiModifierHook.LEFT_CLICK, etshtinkerHook.PLASMA_EXPLOSION_HIT);
    }
    public static float cacheDamage = 0;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot) {
        if (!level.isClientSide){
            createslash(player,tool);
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        cacheDamage = damage;
        return knockback;
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (!context.isExtraAttack() && context.isFullyCharged() && context.getAttacker() instanceof Player player) {
            createslash(player, tool);
        } else if (context.isExtraAttack()&&cacheDamage>0) {
            DamageSource source = new EntityDamageSource("cti.ionized",context.getAttacker()).bypassArmor().bypassMagic();
            context.getTarget().hurt(IafDamageRegistry.causeIndirectDragonLightningDamage(context.getAttacker(), context.getAttacker()), cacheDamage / 2);
            context.getTarget().hurt(source, cacheDamage / 3);
            cacheDamage = 0;
        }
    }

    @Override
    public void afterPlasmaExplosionHit(ToolStack tool, LivingEntity target, plasmaexplosionentity explosion, boolean isCritical) {
        if (!explosion.getTags().contains("slash_created")&&explosion.getOwner() instanceof Player player){
            createslash(player,tool);
            explosion.addTag("slash_created");
        }
    }

    public void createslash(Player player, IToolStackView tool) {
        if (player != null && player.getAttackStrengthScale(0) >= 0.8) {
            Level world = player.level;
            PlasmaWaveSlashProjectile slash = new PlasmaWaveSlashProjectile(CtiEntity.PLASMA_WAVE_SLASH.get(), world);
            world.noCollision(slash);
            slash.setOwner(player);
            slash.setPos(player.getX(), player.getY() + 0.5 * (double) player.getBbHeight(), player.getZ());
            slash.tool = tool;
            Vec3 vec3 = vecCalc.getUnitizedVec3(player.getLookAngle());
            slash.setDeltaMovement(vec3.scale(5.0));
            world.addFreshEntity(slash);
        }
    }

    public static void createSlash(Player player, IToolStackView tool) {
        if (player != null && player.getAttackStrengthScale(0) >= 0.8) {
            Level world = player.level;
            PlasmaWaveSlashProjectile slash = new PlasmaWaveSlashProjectile(CtiEntity.PLASMA_WAVE_SLASH.get(), world);
            world.noCollision(slash);
            slash.setOwner(player);
            slash.setPos(player.getX(), player.getY() + 0.5 * (double) player.getBbHeight(), player.getZ());
            slash.tool = tool;
            Vec3 vec3 = vecCalc.getUnitizedVec3(player.getLookAngle());
            slash.setDeltaMovement(vec3.scale(5.0));
            world.addFreshEntity(slash);
        }
    }
}
