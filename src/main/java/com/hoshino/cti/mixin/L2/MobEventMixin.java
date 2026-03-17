package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.events.MobEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MobEvents.class)
public class MobEventMixin {
    @ModifyArg(
            method = "onMobDeath",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;m_21409_(Lnet/minecraft/world/entity/EquipmentSlot;F)V", ordinal = 0),
            index = 1,
            remap = false
    )
    private static float cti$cancelDropChance(float originalChance) {
        return 0.0F;
    }
}
