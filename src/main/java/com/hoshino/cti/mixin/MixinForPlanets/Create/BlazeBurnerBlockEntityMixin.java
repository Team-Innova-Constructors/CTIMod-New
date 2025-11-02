package com.hoshino.cti.mixin.MixinForPlanets.Create;

import com.hoshino.cti.util.DimensionConstants;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = BlazeBurnerBlockEntity.class)
public class BlazeBurnerBlockEntityMixin {
    @Shadow
    protected BlazeBurnerBlockEntity.FuelType activeFuel;

    @Shadow
    protected int remainingBurnTime;

    @Inject(at = @At(value = "HEAD"), cancellable = true, method = "tick")
    public void tick(CallbackInfo ci) {
        BlazeBurnerBlockEntity entity = (BlazeBurnerBlockEntity) (Object) this;
        Level level = entity.getLevel();
        if (level != null && entity.getActiveFuel() != BlazeBurnerBlockEntity.FuelType.SPECIAL) {
            if (level.dimension().equals(DimensionConstants.INFERNAL)) {
                activeFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
                remainingBurnTime = 20;
            }
        }
    }
}
