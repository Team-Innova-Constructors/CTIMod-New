package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.Cti;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.traits.base.TargetEffectTrait;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.function.Function;

@Mixin(value = TargetEffectTrait.class,remap = false)
public class TargetEffectTraitsMixin {
    @Shadow @Final public Function<Integer, MobEffectInstance> func;

    @Inject(method = "postHurtImpl",at = @At("HEAD"), cancellable = true)
    private void prevent(int level, LivingEntity attacker, LivingEntity target, CallbackInfo ci){
        var modifierID=new ModifierId(Cti.getResource("drflamez"));
        boolean effectCategory= func.apply(1).getEffect()== LCEffects.FLAME.get();
        if(effectCategory&& GetModifierLevel.getTotalArmorModifierlevel(target,modifierID)>0){
            ci.cancel();
        }
    }
}
