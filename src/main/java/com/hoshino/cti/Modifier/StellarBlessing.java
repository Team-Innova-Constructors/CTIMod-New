package com.hoshino.cti.Modifier;

import cofh.core.init.CoreMobEffects;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class StellarBlessing extends EtSTBaseModifier {

    public static float cachedDamage = 0;

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (cachedDamage>0){
            Entity entity = context.getTarget();
            LivingEntity attacker = context.getAttacker();
            if (entity instanceof LivingEntity living) {
                if (living.isOnFire()) {
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.explosion(attacker), damage);
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.FREEZE, damage);
                    living.setSecondsOnFire(0);
                    living.forceAddEffect(new MobEffectInstance(CoreMobEffects.CHILLED.get(), 255, 255, false, false), attacker);
                } else if (living.hasEffect(CoreMobEffects.CHILLED.get()) || living.isInPowderSnow) {
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.explosion(attacker), damage);
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.LAVA, damage);
                    living.setIsInPowderSnow(false);
                    living.setSecondsOnFire(255);
                } else switch (EtSHrnd().nextInt(2)) {
                    case 0 -> {
                        living.invulnerableTime = 0;
                        living.hurt(DamageSource.explosion(attacker), damage);
                        living.invulnerableTime = 0;
                        living.hurt(DamageSource.FREEZE, damage);
                        living.setSecondsOnFire(0);
                        living.forceAddEffect(new MobEffectInstance(CoreMobEffects.CHILLED.get(), 255, 255, false, false), attacker);
                    }
                    case 1 -> {
                        living.invulnerableTime = 0;
                        living.hurt(DamageSource.explosion(attacker), damage);
                        living.invulnerableTime = 0;
                        living.hurt(DamageSource.LAVA, damage);
                        living.setIsInPowderSnow(false);
                        living.setSecondsOnFire(255);
                    }
                }
            }
        }
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        cachedDamage = damage;
        return knockback;
    }
}
