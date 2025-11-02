package com.hoshino.cti.Entity.Projectiles;

import cofh.core.client.particle.options.CylindricalParticleOptions;
import cofh.core.init.CoreParticles;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

import static com.c2h6s.etshtinker.util.vecCalc.*;
import static com.c2h6s.etshtinker.util.vecCalc.getMold;

public class FriendlyMeteor extends Projectile {
    public FriendlyMeteor(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public float lastPartialTick = 0;

    public FriendlyMeteor(Level level, double x, double y, double z, Vec3 movement) {
        this(CtiEntity.FRIENDLY_METEOR.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(movement);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    public float baseDamage;

    @Override
    protected void defineSynchedData() {

    }

    public void tick() {
        Level world1 = this.getLevel();
        Vec3 initVec = this.getDeltaMovement();
        List<LivingEntity> ls = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.25));
        if (!ls.isEmpty()) {
            boolean hit=false;
            ls=this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8));
            for (LivingEntity living : ls) {
                if (living != null && living != this.getOwner()&& !(living instanceof Player)&&world1 instanceof ServerLevel serverLevel) {
                    living.invulnerableTime = 0;
                    living.hurt(DamageSource.explosion(this.getOwner() instanceof LivingEntity living1 ? living1 : null), this.baseDamage);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 1, 0, 0, 0, 0);
                    CylindricalParticleOptions options = new CylindricalParticleOptions(CoreParticles.BLAST_WAVE.get(), 5, 5, 2f);
                    serverLevel.sendParticles(options, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 1, 0, 0, 0, 0);
                    hit = true;
                }
            }
            if (hit) {
                this.playSound(SoundEvents.GENERIC_EXPLODE,0.5f,1.2f);
                this.discard();
                return;
            }
        }
        if (this.tickCount >= 2 && world1 instanceof ServerLevel serverLevel) {
            initVec = initVec.scale(Math.max(0, 1 - 0.025 * this.tickCount));
            LivingEntity living = getNearestLiEnt(32f, this, this.level);
            Vec3 vec3 = Entity1ToEntity2(this, living);
            double trackVelo;
            if (getMold(vec3) != 0) {
                if (getMold(vec3) <= 0.3) {
                    initVec = initVec.scale(0.25);
                }
                trackVelo = Math.min(getMold(vec3), 8 / getMold(vec3));
                trackVelo = Math.min(trackVelo, 0.3);
                trackVelo = Math.max(getMold(living.getDeltaMovement()) + 0.35, trackVelo);
            } else trackVelo = 0;
            vec3 = getUnitizedVec3(vec3).scale(trackVelo);
            vec3 = initVec.add(vec3);
            if (getMold(vec3) >= 1.5) {
                vec3.scale(1.5 / getMold(vec3));
            }
            this.setDeltaMovement(vec3);
        }
        if (this.tickCount > 200) {
            this.remove(RemovalReason.DISCARDED);
        }
        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        if (this.tickCount>1){
            this.level.addParticle(CtiParticleType.STAR_LINE.get(),this.getX(),this.getY(),this.getZ(),initVec.x,initVec.y,initVec.z);
        }

        super.tick();
    }
}
