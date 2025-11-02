package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2complements.events.MaterialDamageListener;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MaterialDamageListener.class)
public class SpaceShardMixin {
    /**
     * @author firefly
     * @reason 去掉原本的空间碎片判定
     */
    @Inject(method = "onDamageFinalized",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;m_19983_(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"), cancellable = true,remap = false)
    private void replace(AttackCache cache, ItemStack weapon, CallbackInfo ci){
        ci.cancel();
    }
}
