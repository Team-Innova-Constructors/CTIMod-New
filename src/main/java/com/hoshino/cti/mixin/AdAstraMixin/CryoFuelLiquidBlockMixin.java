package com.hoshino.cti.mixin.AdAstraMixin;

import earth.terrarium.ad_astra.common.block.fluid.CryoFuelLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.hoshino.cti.content.environmentSystem.EnvironmentalHandler.*;

@Mixin(value = CryoFuelLiquidBlock.class, remap = false)
public class CryoFuelLiquidBlockMixin {
    @Inject(method = "m_7892_", at = @At("HEAD"), cancellable = true)
    private void removeCryoEffect(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity living && getFrozenResistance(living) > 0.5) {
            ci.cancel();
        }
    }
}
