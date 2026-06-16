package com.hoshino.cti.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.hoshino.cti.register.CtiHostilityTrait;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class StrictDamageProcess {
    public static float getStrictDamageForEntity(LivingEntity living,float damage){
        if (living instanceof EntityDragonBase) return 0;
        LazyOptional<MobTraitCap> optional = living.getCapability(MobTraitCap.CAPABILITY);
        if (optional.isPresent()){
            if (optional.resolve().isEmpty()) return damage;
            var mobTrait = optional.resolve().get();
            if (mobTrait.hasTrait(CtiHostilityTrait.EXTREME_DAMAGE_REDUCE.get())) damage*=0.2f;
        }
        return damage;
    }
}
