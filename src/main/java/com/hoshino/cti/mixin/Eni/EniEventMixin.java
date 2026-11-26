package com.hoshino.cti.mixin.Eni;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.hoshino.cti.util.LifeStealCooldown;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnigmaticEventHandler.class, remap = false)
public class EniEventMixin {

    @Unique
    private static final float LIFESTEAL_RATIO = 0.06F;

    @Redirect(method = "onEntityDamaged", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;m_5634_(F)V"))
    private void limitLifeSteal(Player instance, float v) {
        if (LifeStealCooldown.tryTrigger(instance)) {
            return;
        }
        instance.heal(instance.getMaxHealth() * LIFESTEAL_RATIO);
    }
}
