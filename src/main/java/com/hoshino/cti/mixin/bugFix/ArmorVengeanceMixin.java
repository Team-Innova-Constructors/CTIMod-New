package com.hoshino.cti.mixin.bugFix;

import com.james.tinkerscalibration.modifiers.armor.ArmorVengeanceModifier;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArmorVengeanceModifier.class,remap = false)
public class ArmorVengeanceMixin {
    @Inject(at = @At("HEAD"),cancellable = true,method = "onHurt")
    private static void fixCycleHurt(LivingHurtEvent event, CallbackInfo ci){
        if (event.getSource() instanceof EntityDamageSource source&&source.isThorns()) ci.cancel();
    }
}
