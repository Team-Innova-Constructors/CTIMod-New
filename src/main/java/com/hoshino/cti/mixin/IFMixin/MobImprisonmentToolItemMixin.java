package com.hoshino.cti.mixin.IFMixin;

import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MobImprisonmentToolItem.class,remap = false)
public class MobImprisonmentToolItemMixin {
    @Inject(method = "capture",at = @At("HEAD"),cancellable = true)
    private void refuseDeadTargets(ItemStack stack, LivingEntity target, CallbackInfoReturnable<Boolean> cir){
        if (target instanceof IDeadMob mob&&mob.isMobDead()) cir.setReturnValue(false);
    }
}
