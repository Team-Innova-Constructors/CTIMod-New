package com.hoshino.cti.mixin.TIMixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor extends PlayerAccessor{
    @Invoker("tellNeutralMobsThatIDied")
    void tellNeutralMobsThatIDied();
}
