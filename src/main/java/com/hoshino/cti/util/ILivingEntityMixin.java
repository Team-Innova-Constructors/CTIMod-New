package com.hoshino.cti.util;

import net.minecraft.world.damagesource.DamageSource;

public interface ILivingEntityMixin {
    boolean cti$strictHurt(DamageSource pSource, float pAmount,boolean bypassRespawn);

    void cti$actuallyHurt(DamageSource pDamageSource, float pDamageAmount);

    void cti$strictDie(DamageSource pDamageSource,boolean bypassRespawn);
}
