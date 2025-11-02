package com.hoshino.cti.Entity.vehicles;

import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiItem;
import earth.terrarium.ad_astra.common.entity.vehicle.Rocket;
import earth.terrarium.ad_astra.common.registry.ModParticleTypes;
import earth.terrarium.ad_astra.common.util.ModUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class rocketTier5 extends Rocket {
    public rocketTier5(Level level) {
        super(CtiEntity.TIER_5_ROCKET.get(), level, 5);
    }

    public rocketTier5(EntityType<?> type, Level level) {
        super(type, level, 5);
    }

    @Override
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 1.7f;
    }

    @Override
    public boolean shouldSit() {
        return false;
    }

    @Override
    public ItemStack getDropStack() {
        return CtiItem.TIER_5_ROCKET.get().getDefaultInstance();
    }

    @Override
    public void spawnAfterburnerParticles() {
        super.spawnAfterburnerParticles();
        if (this.level instanceof ServerLevel serverWorld) {
            Vec3 pos = this.position();

            float xRotator = Mth.cos(this.getYRot() * ((float) Math.PI / 180.0f)) * 1.2f;
            float zRotator = Mth.sin(this.getYRot() * ((float) Math.PI / 180.0f)) * 1.2f;

            ModUtils.spawnForcedParticles(serverWorld, ModParticleTypes.SMALL_FLAME.get(), pos.x() + xRotator, pos.y() + 0.35, pos.z() + zRotator, 20, 0.1, 0.1, 0.1, 0.001);
            ModUtils.spawnForcedParticles(serverWorld, ModParticleTypes.SMALL_SMOKE.get(), pos.x() + xRotator, pos.y() + 0.35, pos.z() + zRotator, 10, 0.1, 0.1, 0.1, 0.04);

            ModUtils.spawnForcedParticles(serverWorld, ModParticleTypes.SMALL_FLAME.get(), pos.x() - xRotator, pos.y() + 0.35, pos.z() - zRotator, 20, 0.1, 0.1, 0.1, 0.001);
            ModUtils.spawnForcedParticles(serverWorld, ModParticleTypes.SMALL_SMOKE.get(), pos.x() - xRotator, pos.y() + 0.35, pos.z() - zRotator, 10, 0.1, 0.1, 0.1, 0.04);
        }
    }
}
