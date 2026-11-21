package com.hoshino.cti.Entity.Projectiles;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityDragonFireCharge;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.entityTicker.tickers.Fiery;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class RustedFireballEntity extends EntityDragonFireCharge {
    public float damage;

    public RustedFireballEntity(Level pLevel) {
        super(CtiEntity.RUSTED_FIREBALL.get(), pLevel);
    }

    @Override
    public void tick() {
        if (this.tickCount > 5) {
            this.hitAndBlowup();
            return;
        }
        super.tick();
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (this.level.isClientSide) return;
        HitResult.Type hitresult$type = pResult.getType();
        if (hitresult$type == HitResult.Type.ENTITY) {
            this.hitAndBlowup(pResult);
        } else if (hitresult$type == HitResult.Type.BLOCK && this.tickCount > 3) {
            this.hitAndBlowup(pResult);
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        if (pTarget instanceof Projectile||
                pTarget instanceof ItemEntity ||
                pTarget instanceof ExperienceOrb ||
                pTarget==this.getOwner()) return false;
        if (pTarget instanceof LivingEntity){
            if (this.getOwner() instanceof Player player1){
                if (pTarget instanceof Player player) return player1.canHarmPlayer(player);
            }
        }
        return pTarget.isAlive();
    }

    @Override
    protected boolean canHitMob(Entity hitMob) {
        return this.canHitEntity(hitMob);
    }

    public void hitAndBlowup(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.MISS) return;
        Vec3 vec3 = hitResult.getLocation();
        AABB aabb = new AABB(vec3.subtract(6, 3, 6), vec3.add(6, 3, 6));
        if (hitResult instanceof EntityHitResult result) {
            vec3 = result.getEntity().getBoundingBox().getCenter();
        }
        if (this.getOwner() instanceof Player player)
            this.level.getEntitiesOfClass(LivingEntity.class, aabb,
                living -> living != this.getOwner() && !(living instanceof Player)).forEach(living -> {
                living.hurt(ElementalDamageSource.fiery(this.getOwner()), this.damage);
                var fiery = EntityTickerManager.getInstancePlayerSpecific(living, player.getUUID()).getTicker(CtiEntityTickers.FIERY.get());
                if (fiery != null) {
                    Fiery.burntDamage(living, player,fiery.level);
                }
            });
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(CtiParticleType.RED_SPARK.get(), vec3.x, vec3.y, vec3.z, random.nextInt(8) + 8, 0, 0, 0, 0.3);
        }
        this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        this.discard();
    }

    public void hitAndBlowup() {
        if (this.level.isClientSide) return;
        Vec3 vec3 = this.position();
        AABB aabb = new AABB(vec3.subtract(6, 3, 6), vec3.add(6, 3, 6));
        if (this.getOwner() instanceof Player player)
            this.level.getEntitiesOfClass(LivingEntity.class, aabb,
                    living -> living != this.getOwner() && !(living instanceof Player)).forEach(living -> {
                living.hurt(ElementalDamageSource.fiery(this.getOwner()), this.damage);
                var fiery = EntityTickerManager.getInstancePlayerSpecific(living, player.getUUID()).getTicker(CtiEntityTickers.FIERY.get());
                if (fiery != null) {
                    Fiery.burntDamage(living, player,fiery.level);
                }
            });
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.EXPLOSION, vec3.x, vec3.y, vec3.z, 1, 0, 0, 0, 0);
            serverLevel.sendParticles(CtiParticleType.RED_SPARK.get(), vec3.x, vec3.y, vec3.z, random.nextInt(8) + 8, 0, 0, 0, 0.3);
        }
        this.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        this.discard();
    }

    @Override
    public void destroyArea(Level world, BlockPos center, EntityDragonBase destroyer) {
    }

}
