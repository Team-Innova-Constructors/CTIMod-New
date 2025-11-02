package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.entity.projectile.ZephyrSnowball;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ZephyrSnowball.class)
public abstract class ZephyrSnowballMixin extends Fireball implements ItemSupplier {

    public ZephyrSnowballMixin(EntityType<? extends Fireball> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * @author firefly
     * @reason 这B吐泡泡一弹弹十多格诗人啊, 天镜还一直刷，杀都杀不完,搭个裂变产线崩飞800次
     */

    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo ci) {
        this.discard();
        ci.cancel();
    }
}
