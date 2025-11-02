package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CtiSounds {
    public static final DeferredRegister<SoundEvent> sound = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Cti.MOD_ID);
    public static final Supplier<SoundEvent> starHit = sound.register("star_hit", () -> new SoundEvent(Cti.getResource("star_hit")));
}
