package com.hoshino.cti.Entity.Projectiles;

import com.hoshino.cti.Entity.Projectiles.base.BasicHurtingFieldEntity;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.util.ParticleContext;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class VoidArcEntity extends BasicHurtingFieldEntity {
    public VoidArcEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public VoidArcEntity(Level level){
        this(CtiEntity.VOID_ARC.get(), level);
    }

    @Getter
    @Setter
    @Nullable
    public Entity homingEntity = null;

    @Override
    public boolean shouldHurt() {
        return super.shouldHurt()&&this.tickCount%Math.max(5,20/getLvl())==0;
    }

    @Override
    public void hurtEntity(LivingEntity living) {
        if (this.getOwner() instanceof LivingEntity)
            living.hurt(new EntityDamageSource("cti_slime_void",this.getOwner()).bypassArmor().bypassMagic(),getDamage());
    }

    @Override
    public void tick() {
        super.tick();
        if (shouldHurt()&&this.level instanceof ServerLevel serverLevel){
            if (this.getHomingEntity() != null)
                this.setPos(this.getHomingEntity().position().add(0, 0.5f * this.getHomingEntity().getBbHeight(), 0));
            var offset = new Vec3(2 * getRadius() * random.nextDouble()-getRadius(),
                    2 * getRadius() * random.nextDouble()-getRadius(),
                    2 * getRadius() * random.nextDouble()-getRadius());
            ParticleContext.buildParticle(CtiParticleType.VOID_ARC.get()).setPos(position().subtract(offset)).setVelocity(offset.scale(2))
                    .build().sendToClient(serverLevel);
        }
    }

    @Override
    public AABB getRange() {
        return new AABB(this.getX()-getRadius(),this.getY()-getRadius(),this.getZ()-getRadius(), this.getX()+getRadius(),this.getY()+getRadius(),this.getZ()+getRadius());
    }
}
