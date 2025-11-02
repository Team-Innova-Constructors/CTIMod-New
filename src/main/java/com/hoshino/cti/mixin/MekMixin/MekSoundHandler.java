package com.hoshino.cti.mixin.MekMixin;

import mekanism.client.sound.*;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = SoundHandler.class, remap = false)
public abstract class MekSoundHandler {
    /**
     * @author <h3>firefly</h3>
     * @reason <h5>meka创造飞行太鸡儿的吵了,给去掉了</h5>
     */
    @Inject(method = "startSound(Lnet/minecraft/world/level/LevelAccessor;Ljava/util/UUID;Lmekanism/client/sound/PlayerSound$SoundType;)V",
            at = @At(value = "INVOKE",
                    target = "Lmekanism/client/sound/PlayerSound$SoundType;ordinal()I"
            ),
            cancellable = true)
    private static void startSound(LevelAccessor world, UUID uuid, PlayerSound.SoundType soundType, CallbackInfo ci) {
        if (soundType == PlayerSound.SoundType.GRAVITATIONAL_MODULATOR) {
            ci.cancel();
        }
    }
}
