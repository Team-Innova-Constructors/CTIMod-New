package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.traits.common.ShulkerTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ShulkerTrait.class,remap = false)
public class ShulkerMixin {
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;m_7967_(Lnet/minecraft/world/entity/Entity;)Z"), cancellable = true)
    private void mobTick(LivingEntity e, int level, CallbackInfo ci){
        if(e instanceof Mob mob){
            var entity=mob.getTarget();
            if(GetModifierLevel.CurioHasModifierlevel(entity,TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                ci.cancel();
            }
        }
    }
}
