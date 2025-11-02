package com.hoshino.cti.mixin.L2;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EffectUtil.class, remap = false)
public class EffectUtilMixin {
    //L2的强制添加效果会导致效果残留问题，故取消了
    @Inject(at = {@At("HEAD")}, method = {"forceAddEffect"}, cancellable = true)
    private static void cancelEffect(LivingEntity e, MobEffectInstance ins, Entity source, CallbackInfo ci) {
        if (e instanceof Player player) {
            if (ins.getEffect().getCategory() != MobEffectCategory.HARMFUL || !SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE)) {
                e.forceAddEffect(ins, source);
            }
            ci.cancel();
        }
    }
}
