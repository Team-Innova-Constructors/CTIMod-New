package com.hoshino.cti.mixin.DietMixin;

import com.illusivesoulworks.diet.common.capability.PlayerDietTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Map;

@Mixin(PlayerDietTracker.class)
public class EffectMixin {
    @Redirect(
            method = "lambda$applyEffects$2",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            remap = false
    )
    private Object redirectEffect(Map<Object, Object> instance, Object key, Object defaultValue) {
        return 1200;
    }
}
