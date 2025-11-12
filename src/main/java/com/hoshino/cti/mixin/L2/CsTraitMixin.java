package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.traits.goals.CounterStrikeTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CounterStrikeTrait.class)
public class CsTraitMixin {
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Ljava/util/UUID;equals(Ljava/lang/Object;)Z"), cancellable = true,remap = false)
    private void tick(LivingEntity le, int level, CallbackInfo ci){
        if(le instanceof Mob mob){
            if(mob.getTarget() instanceof Player player){
                if(GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())){
                    ci.cancel();
                }
            }
        }
    }
}
