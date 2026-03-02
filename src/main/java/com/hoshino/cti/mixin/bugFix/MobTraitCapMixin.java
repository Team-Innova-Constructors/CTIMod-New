package com.hoshino.cti.mixin.bugFix;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Objects;

@Mixin(value = MobTraitCap.class,remap = false)
public class MobTraitCapMixin {
    @Shadow @Final public LinkedHashMap<MobTrait, Integer> traits;

    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z",shift = At.Shift.BEFORE))
    public void fixNull(LivingEntity mob, CallbackInfo ci){
        traits.keySet().removeIf(Objects::isNull);
    }
}
