package com.hoshino.cti.mixin.MixinForPlanets.Create;

import com.hoshino.cti.util.DimensionConstants;
import com.simibubi.create.content.contraptions.bearing.WindmillBearingBlockEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(remap = false, value = WindmillBearingBlockEntity.class)
public class WindmillBearingBlockEntityMixin {
    @Inject(method = "getGeneratedSpeed", cancellable = true, at = @At(value = "RETURN"))
    public void getGeneratedSpeed(CallbackInfoReturnable<Float> cir) {
        WindmillBearingBlockEntity entity = (WindmillBearingBlockEntity) (Object) this;
        Level level = entity.getLevel();
        float f = cir.getReturnValueF();
        if (level != null && Math.abs(f) > 0) {
            if (level.dimension().equals(DimensionConstants.JUPITER)) {
                cir.setReturnValue(Mth.clamp(64 * f,-256, 256));
            } else if (level.dimension().equals(DimensionConstants.VENUS)) {
                cir.setReturnValue(8 * f);
            } else if (level.dimension().equals(DimensionConstants.URANUS)) {
                cir.setReturnValue(Mth.clamp(24 * f,-256, 256));
            }
        }
    }

}
