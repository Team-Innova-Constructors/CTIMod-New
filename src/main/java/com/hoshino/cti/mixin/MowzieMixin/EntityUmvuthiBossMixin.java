package com.hoshino.cti.mixin.MowzieMixin;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieGeckoEntity;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.EntityUmvuthi;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityUmvuthi.class)
public abstract class EntityUmvuthiBossMixin extends MowzieGeckoEntity {
    public EntityUmvuthiBossMixin(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Inject(method = "<init>",at = @At("RETURN"))
    private void entity(EntityType<? extends EntityUmvuthi> type, Level world, CallbackInfo ci){
        this.getPersistentData().putInt("umvuthi_summon",3);
    }
}
