package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.common.AuraEffectTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AuraEffectTrait.class)
public class GraMixin {
    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    private void tick(LivingEntity mob, int level, CallbackInfo ci){
        if(mob instanceof Mob mob1){
            LazyOptional<MobTraitCap> optional = mob1.getCapability(MobTraitCap.CAPABILITY);
            if (optional.resolve().isEmpty()) return;
            MobTraitCap cap = optional.resolve().get();
            MobTrait trait = CtiHostilityTrait.PURIFYTRAIT.get();
            if (!cap.hasTrait(trait))return;
            ci.cancel();
        }
    }
}
