package com.hoshino.cti.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("markHurt")
    void markHurt();
    @Invoker("reapplyPosition")
    void reapplyPosition();

}
