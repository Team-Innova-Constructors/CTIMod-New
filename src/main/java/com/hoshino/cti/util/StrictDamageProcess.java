package com.hoshino.cti.util;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.hoshino.cti.register.CtiHostilityTrait;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class StrictDamageProcess {
    public static float getStrictDamageForEntity(LivingEntity living,float damage){
        if (living instanceof EntityDragonBase) return 0;
        var cap = living.getCapability(MobTraitCap.CAPABILITY);
        if (cap.isPresent()){
            var mobTrait = cap.orElse(null);
            if (mobTrait.hasTrait(CtiHostilityTrait.EXTREME_DAMAGE_REDUCE.get())) damage*=0.4f;
        }
        return damage;
    }
}
