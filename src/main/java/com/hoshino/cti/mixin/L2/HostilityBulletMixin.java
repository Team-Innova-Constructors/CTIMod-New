package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HostilityBullet.class)
public abstract class HostilityBulletMixin extends ShulkerBullet {
    public HostilityBulletMixin(EntityType<? extends ShulkerBullet> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.isExplosion())return false;
        if(pSource.getDirectEntity() instanceof Projectile){
            ItemEntity shard=new ItemEntity(this.level,this.getX(),this.getY(),this.getZ(),new ItemStack(LCItems.EXPLOSION_SHARD.get()));
            this.level.explode(null,this.getX(),this.getY(),this.getZ(),1, Explosion.BlockInteraction.NONE);
            this.level.addFreshEntity(shard);
        }
        return super.hurt(pSource,pAmount);
    }
    @Unique
    @Override
    public void tick() {
        super.tick();
        Vec3 vec31 = this.getDeltaMovement();
        this.level.addParticle(ParticleTypes.FLAME, this.getX() - vec31.x, this.getY() - vec31.y + 0.15D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
    }
}
