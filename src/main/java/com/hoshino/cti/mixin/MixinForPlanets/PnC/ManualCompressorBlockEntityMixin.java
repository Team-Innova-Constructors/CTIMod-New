package com.hoshino.cti.mixin.MixinForPlanets.PnC;

import com.hoshino.cti.util.DimensionConstants;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandler;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.block.entity.ManualCompressorBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = ManualCompressorBlockEntity.class)
public class ManualCompressorBlockEntityMixin {
    @Inject(cancellable = true, method = "tickServer", at = @At(value = "HEAD"))
    public void tickServer(CallbackInfo ci) {
        ManualCompressorBlockEntity entity = (ManualCompressorBlockEntity) (Object) this;
        Level level = entity.getLevel();
        if (level != null && level.dimension().equals(DimensionConstants.JUPITER)) {
            LazyOptional<IAirHandlerMachine> optional = entity.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, entity.getRotation());
            IAirHandler handler = optional.orElse(null);
            int MaxAmount = (int) (handler.getBaseVolume() * 4.9);
            if (handler.getPressure() < 4.9) {
                handler.addAir((int) ((MaxAmount - handler.getAir()) * 0.75f));
            }
        }

    }
}
