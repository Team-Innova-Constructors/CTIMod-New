package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.backport.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(value = MobTraitCap.class,remap = false)
public abstract class MobTraitCapMixin extends GeneralCapabilityTemplate<LivingEntity, MobTraitCap> {
    @Shadow @Final public LinkedHashMap<MobTrait, Integer> traits;

    @Redirect(method = "tick(Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;removeIf(Ljava/util/function/Predicate;)Z", ordinal = 0))
    private boolean l2hostility$safeRemoveIf(Set<MobTrait> instance, Predicate<? super MobTrait> filter) {
        if (this.traits.containsKey(null)) {
            this.traits.remove(null);
        } 
        return instance.removeIf(MobTrait::isBanned);
    }
}
