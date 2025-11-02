package com.hoshino.cti.mixin.TIMixin;

import com.hoshino.cti.mixin.LivingEntityAccessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerAccessor extends LivingEntityAccessor {
    @Invoker("removeEntitiesOnShoulder")
    void removeEntitiesOnShoulder();
}
