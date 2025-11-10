package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CtiSounds {
    public static final DeferredRegister<SoundEvent> sound = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Cti.MOD_ID);
    public static final Supplier<SoundEvent> starHit = sound.register("star_hit", () -> new SoundEvent(Cti.getResource("star_hit")));
    public static final Supplier<SoundEvent> superDie1 = sound.register("super_die1", () -> new SoundEvent(Cti.getResource("super_die1")));
    public static final Supplier<SoundEvent> superDie2 = sound.register("super_die2", () -> new SoundEvent(Cti.getResource("super_die2")));
    public static final Supplier<SoundEvent> armor_broken = sound.register("armor_broken", () -> new SoundEvent(Cti.getResource("armor_broken")));
    public static final Supplier<SoundEvent> location_exposed = sound.register("location_exposed", () -> new SoundEvent(Cti.getResource("location_exposed")));
}
