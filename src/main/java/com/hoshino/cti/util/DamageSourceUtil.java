package com.hoshino.cti.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class DamageSourceUtil {
    public static EntityDamageSource sourced(DamageSource source, Entity entity, @Nullable LivingEntity cause){
        return new IndirectEntityDamageSource(source.msgId,entity,cause);
    }
}
