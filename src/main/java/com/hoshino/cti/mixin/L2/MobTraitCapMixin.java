package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(value = MobTraitCap.class,remap = false)
public class MobTraitCapMixin {
    @ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z"))
    public Predicate<MobTrait> fixRemoveifCrash(Predicate<MobTrait> par1){
        return mobTrait -> mobTrait.isBanned();
    }
}
