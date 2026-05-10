package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.armor.ArmorRedHeart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = ArmorRedHeart.class,remap = false)
public class ArmorRedHeartMixin {
    @ModifyArg(method = "getBonus",at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"),index = 0)
    public float modifyMaxBonus(float value){
        return 0.15f;
    }
}
