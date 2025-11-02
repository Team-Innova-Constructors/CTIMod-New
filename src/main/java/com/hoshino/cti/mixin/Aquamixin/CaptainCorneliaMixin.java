package com.hoshino.cti.mixin.Aquamixin;

import com.obscuria.aquamirae.common.entities.CaptainCornelia;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = CaptainCornelia.class, remap = false)
public abstract class CaptainCorneliaMixin extends Monster {
    protected CaptainCorneliaMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Unique
    @Override
    protected boolean canRide(Entity pVehicle) {
        return false;
    }
}
