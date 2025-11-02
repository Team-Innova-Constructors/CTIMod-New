package com.hoshino.cti.mixin.bugFix;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedcore.upgrades.feeding.FeedingUpgradeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FeedingUpgradeWrapper.class,remap = false)
public class FeedingUpgradeWrapperMixin {
    @Inject(at = @At("HEAD"),method = "isEdible",cancellable = true)
    private static void avoidNotStackable(ItemStack stack, LivingEntity player, CallbackInfoReturnable<Boolean> cir){
        if (stack.getItem().getMaxStackSize()<64) cir.setReturnValue(false);
    }
}
