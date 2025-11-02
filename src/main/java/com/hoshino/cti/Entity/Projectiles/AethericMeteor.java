package com.hoshino.cti.Entity.Projectiles;

import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.register.CtiEntity;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;


public class AethericMeteor extends Projectile{
    public AethericMeteor(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public AethericMeteor(Level level, double x, double y, double z, Vec3 movement) {
        this(CtiEntity.FRIENDLY_METEOR.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(movement);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (this.tickCount > 1200) this.discard();
        if (this.getDeltaMovement().length() > 1) {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        }
        this.setDeltaMovement(this.getDeltaMovement().add(0, -0.25, 0));
        HitResult hitresult = this.level.clip(new ClipContext(this.position(), this.position().add(this.getDeltaMovement()), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level, this, this.position(), this.position().add(this.getDeltaMovement()), this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.5), this::canHitEntity);
        super.move(MoverType.SELF, this.getDeltaMovement());
        if (this.tickCount>1){
            this.level.addParticle(CtiParticleType.STAR_LINE.get(),this.getX(),this.getY(),this.getZ(),this.getDeltaMovement().x,this.getDeltaMovement().y,this.getDeltaMovement().z);
        }
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }
        if (this.onGround) {
            this.onHit(new BlockHitResult(this.position(), Direction.UP, this.blockPosition().below(), false));
        }
        super.tick();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
        if (this.level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,this.getX(),this.getY(),this.getZ(),10,0,0,0,1.5);
            if (random.nextInt(16)==0) {
                ItemEntity entity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(TIItems.STAR_ALLOY_INGOT.get()));
                serverLevel.addFreshEntity(entity);
            }
        }
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
        if (this.level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,this.getX(),this.getY(),this.getZ(),10,0,0,0,1.5);
            pResult.getEntity().invulnerableTime=0;
            pResult.getEntity().hurt(DamageSource.explosion((LivingEntity) null),90);
            if (random.nextInt(16)==0) {
                ItemEntity entity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(TIItems.STAR_ALLOY_INGOT.get()));
                serverLevel.addFreshEntity(entity);
            }
        }
        this.discard();
    }
}
