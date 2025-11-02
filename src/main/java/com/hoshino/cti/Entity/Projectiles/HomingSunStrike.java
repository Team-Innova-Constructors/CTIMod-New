package com.hoshino.cti.Entity.Projectiles;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.client.particle.ParticleOrb;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntitySunstrike;
import com.hoshino.cti.util.ISunStrikeMixin;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class HomingSunStrike extends EntitySunstrike {
    public @Nullable LivingEntity homingEntity;
    public LivingEntity owner;
    public float damage = 16;
    private static final EntityDataAccessor<Integer> STRIKE_COUNT = SynchedEntityData.defineId(HomingSunStrike.class, EntityDataSerializers.INT);
    public HomingSunStrike(EntityType<? extends EntitySunstrike> type, Level world) {
        super(type, world);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STRIKE_COUNT,0);
    }

    public void setStrikeCount(int count){
        this.entityData.set(STRIKE_COUNT,count);
    }
    public int getStrikeCount(){
        return this.entityData.get(STRIKE_COUNT);
    }

    @Override
    public void tick() {
        this.baseTick();
        this.setPrevStrikeTime(this.getStrikeTime());
        if (this.level.isClientSide) {
            if (this.getStrikeTime() == 0) {
                MowziesMobs.PROXY.playSunstrikeSound(this);
            } else if (this.getStrikeTime() < 25) {
                float time = this.getStrikeTime(1.0F);
                int timeBonus = (int)(time * 5.0F);
                int orbCount = this.random.nextInt(4 + timeBonus) + timeBonus + 1;

                while(orbCount-- > 0) {
                    float theta = this.random.nextFloat() * ((float)Math.PI * 2F);
                    float r = this.random.nextFloat() * 1.6999999F + 0.2F;
                    float ox = r * Mth.cos(theta);
                    float oz = r * Mth.sin(theta);
                    float oy = this.random.nextFloat() * (time * 6.0F - 0.1F) + 0.1F;
                    this.level.addParticle(new ParticleOrb.OrbData((float)this.getX(), (float)this.getZ()), this.getX() + (double)ox, this.getY() + (double)oy, this.getZ() + (double)oz, (double)0.0F, (double)0.0F, (double)0.0F);
                }
            } else if (this.getStrikeTime() > 35) {
                this.smolder();
                if (getStrikeTime()>43&&this.getStrikeCount()>0){
                    this.setStrikeCount(this.getStrikeCount()-1);
                    this.setStrikeTime(34);
                    this.setPrevStrikeTime(33);
                }
            } else if (this.getStrikeTime() == 35) {
                this.spawnExplosionParticles(10);
            }
        } else {
            if (this.getStrikeTime()<44) {
                this.moveDownToGround();
            }
            if (getStrikeTime() < 400) {
                if (getStrikeTime() == 35) {
                    this.damageEntityLivingBaseNearby(3.0F);
                }
                if (getStrikeTime()>43&&this.getStrikeCount()>0){
                    this.setStrikeCount(this.getStrikeCount()-1);
                    this.setStrikeTime(34);
                    this.setPrevStrikeTime(33);
                }
            } else {
                this.discard();
            }
        }

        this.setStrikeTime(this.getStrikeTime()+1);
    }

    public int getStrikeTime(){
        return ((ISunStrikeMixin)this).cti$getStrikeTime();
    }

    public int getPrevStrikeTime(){
        return ((ISunStrikeMixin)this).cti$getPrevStrikeTime();
    }

    public void setStrikeTime(int i) {
        ((ISunStrikeMixin)this).cti$setStrikeTime(i);
    }


    public void setPrevStrikeTime(int i) {
        ((ISunStrikeMixin)this).cti$setPrevStrikeTime(i);
    }
    private void spawnExplosionParticles(int amount) {
        for(int i = 0; i < amount; ++i) {
            float yaw = (float)i * (((float)Math.PI * 2F) / (float)amount);
            float vy = this.random.nextFloat() * 0.08F;
            float vx = 0.1F * Mth.cos(yaw);
            float vz = 0.1F * Mth.sin(yaw);
            this.level.addParticle(ParticleTypes.FLAME, this.getX(), this.getY() + 0.1, this.getZ(), (double)vx, (double)vy, (double)vz);
        }

        for(int i = 0; i < amount / 2; ++i) {
            this.level.addParticle(ParticleTypes.LAVA, this.getX(), this.getY() + 0.1, this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
        }

    }

    private void smolder() {
        if (this.random.nextFloat() < 0.1F) {
            int amount = this.random.nextInt(2) + 1;

            while(amount-- > 0) {
                float theta = this.random.nextFloat() * ((float)Math.PI * 2F);
                float r = this.random.nextFloat() * 0.7F;
                float x = r * Mth.cos(theta);
                float z = r * Mth.sin(theta);
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (double)x, this.getY() + 0.1, this.getZ() + (double)z, (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }

    }

    @Override
    public void moveDownToGround() {
        if (this.homingEntity!=null&&this.homingEntity.isDeadOrDying()) this.homingEntity=null;
        if (this.homingEntity==null){
            AABB aabb = new AABB(this.getX()-8,this.getY()-8,this.getZ()-8,this.getX()+8,this.getY()+8,this.getZ()+8);
            this.level.getEntitiesOfClass(LivingEntity.class, aabb, living -> living != this.owner&&!(living instanceof Player))
                    .stream()
                    .min((living1, living2) ->
                    (int) ((living1.position().subtract(this.position()).length() - living2.position().subtract(this.position()).length()) * 100))
                    .ifPresent(living -> this.homingEntity = living);
        }
        if (this.homingEntity!=null){
            this.setPos(homingEntity.position());
        }
    }

    @Override
    public void damageEntityLivingBaseNearby(double radius) {
        AABB aabb = new AABB(this.getX()-radius,this.getY()-0.5,this.getZ()-radius,this.getX()+radius,this.getY()+20,this.getZ()+radius);
        this.level.getEntitiesOfClass(LivingEntity.class,aabb,living -> living!=this.owner).forEach(living -> {
            living.invulnerableTime = 0;
            living.hurt(DamageSource.indirectMobAttack(this,this.homingEntity),this.damage);
            living.invulnerableTime = 0;
            DamageSource fireSource = new IndirectEntityDamageSource(DamageSource.IN_FIRE.msgId,this,this.owner).setIsFire();
            living.hurt(fireSource,this.damage);
        });
    }
}
