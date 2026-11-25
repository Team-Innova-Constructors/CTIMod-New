package com.hoshino.cti.mixin.Eni;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


@Mixin(PiglinAi.class)
public interface PiglinAiAccessor {
    @Invoker("wasHurtBy")
    static void cti$wasHurtBy(Piglin piglin, LivingEntity target) {
        throw new AssertionError();
    }
}
