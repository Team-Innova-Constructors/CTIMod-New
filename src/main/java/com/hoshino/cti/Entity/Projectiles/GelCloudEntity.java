package com.hoshino.cti.Entity.Projectiles;

import cofh.core.init.CoreMobEffects;
import com.hoshino.cti.register.CtiEntity;
import com.mojang.math.Vector3f;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.particle.FluidParticleData;

public class GelCloudEntity extends Projectile {
    public GelCloudEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.noPhysics=true;
    }
    public GelCloudEntity(Level level){
        this(CtiEntity.GEL_CLOUD.get(), level);
    }

    @Override
    public boolean isInvisible() {
        return true;
    }

    public float damage=0;
    public float radius=4;

    @Override
    public void tick() {
        super.tick();
        var visualRadius = radius*Math.min(1,this.tickCount/10f);
        this.tickCount++;
        if (tickCount>200) this.discard();
        if (!this.isRemoved()){
            if (this.tickCount%5==0&&!level.isClientSide&&this.damage>0&&this.getOwner() instanceof LivingEntity living){
                this.level.getEntitiesOfClass(LivingEntity.class,new AABB(this.getX()-visualRadius,this.getY()-1,this.getZ()-visualRadius, this.getX()+visualRadius,this.getY()+1,this.getZ()+visualRadius),entity->
                        entity!=living&&!(entity instanceof Player)&&entity.isAlive()).forEach(living1 -> {
                            living1.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,200,2),living);
                            living1.forceAddEffect(new MobEffectInstance(CoreMobEffects.SUNDERED.get(),200,2),living);
                            var it = living1.invulnerableTime;
                            var lastHurt = living1.lastHurt;
                            living1.invulnerableTime = 0;
                            living1.hurt(DamageSource.indirectMagic(this,living),this.damage);
                            living1.invulnerableTime = it;
                            living1.lastHurt = lastHurt;
                });
            }
            if (this.level instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(new FluidParticleData(TinkerCommons.fluidParticle.get(), new FluidStack(TinkerFluids.skySlime.get(),1)),this.getX(),this.getY(),this.getZ(),this.tickCount<10?40:10,visualRadius,1,visualRadius,0.05);
            }
        }
    }

    @Override
    protected void defineSynchedData() {

    }
}
