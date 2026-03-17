package com.hoshino.cti.Entity.Projectiles.base;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class BasicHurtingFieldEntity extends Projectile {
    public BasicHurtingFieldEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.noPhysics=true;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    @Getter
    @Setter
    protected float damage=0;
    @Getter
    @Setter
    protected float radius=4;
    @Getter
    @Setter
    protected int lvl = 1;
    @Getter
    @Setter
    protected int maxTicks = 100;

    @Override
    public void tick() {
        super.tick();
        this.tickCount++;
        if (tickCount>getMaxTicks()) this.discard();
        if (!this.isRemoved()){
            if (shouldHurt()){
                this.level.getEntitiesOfClass(LivingEntity.class,getRange(), this::canHitEntity)
                        .forEach(living1 -> {
                    var it = living1.invulnerableTime;
                    var lastHurt = living1.lastHurt;
                    living1.invulnerableTime = 0;
                    hurtEntity(living1);
                    living1.invulnerableTime = it;
                    living1.lastHurt = lastHurt;
                });
            }
        }
    }

    public boolean shouldHurt(){
        return !level.isClientSide&&getDamage()>0&&getRadius()>0&&this.getOwner() instanceof LivingEntity;
    }

    public void hurtEntity(LivingEntity living){

    }

    public AABB getRange(){
        return new AABB(this.getX()-getRadius(),this.getY()-1,this.getZ()-getRadius(), this.getX()+getRadius(),this.getY()+1,this.getZ()+getRadius());
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (this.getOwner() instanceof LivingEntity living){
            return entity!=living&&!(entity instanceof Player)&&entity.isAlive();
        }
        return entity instanceof LivingEntity||entity instanceof ShulkerBullet;
    }

    @Override
    protected void defineSynchedData() {

    }
}
