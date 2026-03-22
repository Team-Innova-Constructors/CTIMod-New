package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.events.MobEvents;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

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
    @Redirect(
            method = "onMobDeath",
            at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/compat/curios/CurioCompat;hasItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/Item;)Z"),
            remap = false
    )
    private static boolean cti$addExtraItemCheck(LivingEntity entity, Item item) {
        boolean hasOriginal = CurioCompat.hasItem(entity, item);
        boolean hasNewItem = CurioCompat.hasItem(entity, LHItems.NIDHOGGUR.get());
        return hasOriginal || hasNewItem;
    }
}
