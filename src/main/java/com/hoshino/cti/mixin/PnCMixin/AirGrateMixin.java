package com.hoshino.cti.mixin.PnCMixin;

import me.desht.pneumaticcraft.common.tubemodules.AirGrateModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AirGrateModule.class, remap = false)
public class AirGrateMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lme/desht/pneumaticcraft/common/tubemodules/AirGrateModule;pushEntities(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/Vec3;)V"), method = "tickCommon")
    private void stopPushing(AirGrateModule instance, Level y, BlockPos entityPos, Vec3 z) {
    }
}
