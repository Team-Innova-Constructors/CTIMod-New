package com.hoshino.cti.Modifier.genre.insatiable;

import com.hoshino.cti.Modifier.genre.insatiable.forTrait.InsatiableHandler;
import com.hoshino.cti.register.CtiEffects;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.NamespacedNBT;
import slimeknights.tconstruct.tools.stats.ToolType;

public class ArroganceInsatiable extends BasicInsatiableModifier{
    @Override
    public int getInsatiableLevel() {
        return 6;
    }

    @Override
    public float getMaxInsatiableBonus(IToolContext context, ModifierEntry modifier) {
        return 72*modifier.getLevel();
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getTarget() instanceof LivingEntity living&&context.isFullyCharged()) {
            living.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap -> {
                if (cap.lv >= 256) {
                    InsatiableHandler.applyEffect(context.getAttacker(), ToolType.MELEE,16);
                }
                if (cap.lv>=512){
                    context.getAttacker().addEffect(new MobEffectInstance(CtiEffects.INSATIABLE_BOOST.get(),300,63,false,false));
                }
                if (cap.lv>=1024){
                    int it = living.invulnerableTime;
                    float lastHurt = living.lastHurt;
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.mobAttack(context.getAttacker()),128);
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.mobAttack(context.getAttacker()),128);
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.mobAttack(context.getAttacker()),128);
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.mobAttack(context.getAttacker()),128);
                    living.invulnerableTime = it;
                    living.lastHurt = lastHurt;
                }
            });
        }
    }

    @Override
    public boolean modifierOnProjectileHitEntity(ModifierNBT modifiers, NamespacedNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (attacker!=null&&target!=null&&projectile instanceof AbstractArrow arrow&&arrow.isCritArrow())
            target.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap -> {
                if (cap.lv >= 256) {
                    InsatiableHandler.applyEffect(attacker, ToolType.MELEE,16);
                }
                if (cap.lv>=512){
                    attacker.addEffect(new MobEffectInstance(CtiEffects.INSATIABLE_BOOST.get(),300,63,false,false));
                }
                if (cap.lv>=1024){
                    int it = target.invulnerableTime;
                    float lastHurt = target.lastHurt;
                    target.invulnerableTime = 0;
                    target.hurt(DamageSource.mobAttack(attacker),128);
                    target.invulnerableTime = 0;
                    target.hurt(DamageSource.mobAttack(attacker),128);
                    target.invulnerableTime = 0;
                    target.hurt(DamageSource.mobAttack(attacker),128);
                    target.invulnerableTime = 0;
                    target.hurt(DamageSource.mobAttack(attacker),128);
                    target.invulnerableTime = it;
                    target.lastHurt = lastHurt;
                }
            });
        return false;
    }
}
