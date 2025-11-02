package com.hoshino.cti.Entity.Projectiles;

import com.hoshino.cti.register.CtiEntityData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BaseFallenAmmo extends Projectile implements ItemSupplier {
    protected static final EntityDataAccessor<BlockPos> targetPosition = SynchedEntityData.defineId(BaseFallenAmmo.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Integer> waitTick= SynchedEntityData.defineId(BaseFallenAmmo.class,EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> hurtDamage= SynchedEntityData.defineId(BaseFallenAmmo.class,EntityDataSerializers.FLOAT);
    protected boolean isArrived;
    protected int arrivedTime;
    protected boolean hasBeenSetSpeed;
    protected BaseFallenAmmo(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    protected BaseFallenAmmo(EntityType<? extends BaseFallenAmmo> pEntityType, LivingEntity owner, Level level, BlockPos targetPosition){
        super(pEntityType,level);
        setOwner(owner);
        this.setTargetPosition(targetPosition);
        var source=this.getLevel().random;
        this.setPos(targetPosition.getX() + source.nextInt(30),targetPosition.getY() + 50,targetPosition.getZ() + source.nextInt(30));
        this.setNoGravity(true);
        this.setDeltaMovement(getVec3TargetPosition().subtract(this.position()).scale(0.02f));
    }
    @Override
    protected void defineSynchedData() {
        this.entityData.define(targetPosition,new BlockPos( 0,0,0));
        this.entityData.define(waitTick,0);
        this.entityData.define(hurtDamage,5f);
    }
    public void setHurtDamage(float damage){
        this.entityData.set(hurtDamage,damage);
    }
    public float getHurtDamage(){
        return this.entityData.get(hurtDamage);
    }
    public void setWaitTime(int tick){
        this.entityData.set(waitTick,tick);
    }
    public int getWaitTime(){
        return this.entityData.get(waitTick);
    }
    public Vec3 getVec3TargetPosition(){
        return new Vec3(getTargetPosition().getX(),getTargetPosition().getY(),getTargetPosition().getZ());
    }
    public BlockPos getTargetPosition(){
        return this.entityData.get(targetPosition);
    }
    public void setTargetPosition(BlockPos position){
        this.entityData.set(targetPosition,new BlockPos(position));
    }
    protected void onArrived(ServerPlayer player){
        this.setDeltaMovement(Vec3.ZERO);
        this.isArrived=true;
    }
    protected void directHurtLiving(@Nullable DamageSource source,float amount,double range){
        if(source!=null&&range>0){
            var vec3=getVec3TargetPosition();
            int x= (int) vec3.x();
            int y= (int) vec3.y();
            int z= (int) vec3.z();
            List<Mob>livingEntities=this.getLevel().getEntitiesOfClass(Mob.class,new AABB(new BlockPos(x,y,z)).inflate(range),lv->{
                boolean base=lv!=getOwner()&&lv.isAlive();
                boolean isPet=lv instanceof TamableAnimal animal&&animal.isTame();
                return base&&!isPet;
            });
            for(Mob lv:livingEntities){
                lv.hurt(source,amount);
            }
        }
    }
    protected abstract void shockWaveHurt(Mob mob, Player player);
    private void commonTick() {
        this.noPhysics=true;
        if (this.isArrived) {
            arrivedTime++;
        }
        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level.getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this, (TheEndGatewayBlockEntity) blockentity);
                }
                flag = true;
            }
        }
        if (hitresult.getType() != HitResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }
        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25D, d0 - vec3.y * 0.25D, d1 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }
        }
        if (!this.isNoGravity()) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y, vec31.z);
        }
        this.setPos(d2, d0, d1);
    }

    protected abstract @NotNull ParticleOptions getTailParticleType();

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 200) {
            this.discard();
        }
        if (this.getWaitTime() > 0) {
            this.setWaitTime(this.getWaitTime() - 1);
            return;
        }
        commonTick();
        if (this.getWaitTime() == 0 || !hasBeenSetSpeed) {
            hasBeenSetSpeed = true;
        }
        Vec3 vec3 = this.getDeltaMovement();
        if (this.level.isClientSide) return;
        if (this.getOwner() instanceof ServerPlayer player) {
            for (int i = 0; i < 4; ++i) {
                double x = vec3.x;
                double y = vec3.y;
                double z = vec3.z;
                player.getLevel().sendParticles(getTailParticleType(), this.getX() + x * (double) i / 4.0D, this.getY() + y * (double) i / 4.0D, this.getZ() + z * (double) i / 4.0D, 10, 0, 0, 0, 0.25);
            }
            if (this.position().distanceTo(getVec3TargetPosition())<1d && !isArrived) {
                onArrived(player);
            }
            if (isArrived && arrivedTime < 80) {
                var Vec3Pos = getVec3TargetPosition();
                var pos = new BlockPos((int) Vec3Pos.x(), (int) Vec3Pos.y(), (int) Vec3Pos.z());
                AABB aabb = new AABB(pos).inflate(100);
                List<Mob> mobList = this.level.getEntitiesOfClass(Mob.class, aabb, LivingEntity::isAlive);
                for (Mob mob : mobList) {
                    var distance = mob.position().subtract(getVec3TargetPosition()).length();
                    if (distance < arrivedTime - 3 || distance > arrivedTime + 3) continue;
                    this.shockWaveHurt(mob, player);
                }
            }
        }
    }
}
