package com.hoshino.cti.Entity.Projectiles;

import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FieryJavelinProjectile extends AbstractArrow {
    public FieryJavelinProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setBaseDamage(16);
    }

    public FieryJavelinProjectile(Level level, LivingEntity pShooter){
        super(CtiEntity.FIERY_JAVELIN.get(),pShooter,level);
        this.setBaseDamage(16);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    public void explode(Entity target){
        Vec3 position = target.getBoundingBox().getCenter();
        AABB aabb = new AABB(position.x-1,position.y-1,position.z-1,position.x+1,position.y+1,position.z+1);
        this.level.getEntitiesOfClass(Entity.class,aabb,entity -> !(entity instanceof ItemEntity)&&!(entity instanceof ExperienceOrb)&&entity!=this.getOwner()).forEach(entity ->{
            entity.invulnerableTime =0;
            entity.hurt(IafDamageRegistry.causeIndirectDragonFireDamage(this,this.getOwner()),(float) this.getBaseDamage());
        });
        this.playSound(SoundEvents.FIRECHARGE_USE);
        this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
        if (this.level instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(CtiParticleType.FIERY_EXPLODE.get(),position.x,position.y,position.z,1,0,0,0,0);
            serverLevel.sendParticles(CtiParticleType.RED_SPARK.get(),position.x,position.y,position.z,16,0,0,0,0.4);
        }
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().scale(2);
        this.setBaseDamage(this.getBaseDamage()*pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, pShooter.isOnGround() ? 0.0D : vec3.y, vec3.z));
    }

    @Override
    public void tick() {
        if (this.firstTick) this.setPierceLevel((byte) (this.getPierceLevel()+1));
        this.tickCount++;
        if (this.tickCount>800) this.discard();
        Vec3 velocity = this.getDeltaMovement();
        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(this.level, this, this.position(),this.position().add(this.getDeltaMovement().scale(5)), this.getBoundingBox().expandTowards(this.getDeltaMovement().scale(5)).inflate(1.0D), entity -> entity != this.getOwner() && this.canHitEntity(entity));
        if (hitResult!=null){
            net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitResult);
            this.onHit(hitResult);
        }
        this.setDeltaMovement(velocity);
        this.setPos(this.position().add(this.getDeltaMovement().scale(2)));
        this.level.addParticle(CtiParticleType.FIERY_LINE.get(),this.getX(),this.getY()+0.5*this.getBbHeight(),this.getZ(),this.getDeltaMovement().x*5,this.getDeltaMovement().y*5,this.getDeltaMovement().z*5);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.explode(pResult.getEntity());
    }
}
