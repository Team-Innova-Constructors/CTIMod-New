package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2hostility.content.traits.highlevel.SplitTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SplitTrait.class,remap = false)
public class SplitTraitMixin {
    @Inject(method = "onDeath",at = @At(value = "INVOKE", target = "Ldev/xkmc/l2hostility/content/traits/highlevel/SplitTrait;add(Lnet/minecraft/world/entity/LivingEntity;)V"), cancellable = true)
    private void onDeath(int lv, LivingEntity entity, LivingDeathEvent event, CallbackInfo ci){
        if(event.getSource().getEntity() instanceof LivingEntity living&& GetModifierLevel.HandsHaveModifierlevel(living, CtiModifiers.doNotSplitStaticModifier.getId())){
            ci.cancel();
        }
    }
}
