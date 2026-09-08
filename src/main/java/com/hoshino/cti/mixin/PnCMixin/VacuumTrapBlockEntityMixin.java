package com.hoshino.cti.mixin.PnCMixin;

import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.common.block.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VacuumTrapBlockEntity.class,remap = false)
public abstract class VacuumTrapBlockEntityMixin extends AbstractAirHandlingBlockEntity{
    public VacuumTrapBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier pressureTier, int volume, int upgradeSlots) {
        super(type, pos, state, pressureTier, volume, upgradeSlots);
    }

    @Inject(method = "isApplicable",at = @At("HEAD"),cancellable = true)
    private void disallowDeadMobs(LivingEntity e, CallbackInfoReturnable<Boolean> cir){
        if (e instanceof IDeadMob mob&&mob.isMobDead()) cir.setReturnValue(false);
    }
}
