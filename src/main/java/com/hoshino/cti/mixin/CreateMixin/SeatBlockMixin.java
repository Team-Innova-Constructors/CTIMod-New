package com.hoshino.cti.mixin.CreateMixin;

import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SeatBlock.class,remap = false)
public class SeatBlockMixin {
    @Inject(method = "sitDown",cancellable = true,at = @At("HEAD"))
    private static void disallowHighLvToSeat(Level world, BlockPos pos, Entity entity, CallbackInfo ci){
        if (entity instanceof LivingEntity living&&!(living instanceof Player)){
            living.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap->{
                if (cap.lv>49) ci.cancel();
            });
        }
    }
}
