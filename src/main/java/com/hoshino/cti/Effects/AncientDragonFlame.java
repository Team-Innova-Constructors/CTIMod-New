package com.hoshino.cti.Effects;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AncientDragonFlame extends StaticMobEffect {
    public AncientDragonFlame() {
        super(MobEffectCategory.BENEFICIAL, 0xff6f08);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        int count = living.tickCount;
        if (count % 20 == 0) {
            living.hurt(DamageSource.indirectMobAttack(living, living).bypassInvul().bypassArmor().bypassMagic(), living.getMaxHealth() * 0.03f);
        }
    }
}
