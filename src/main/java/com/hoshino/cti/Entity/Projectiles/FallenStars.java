package com.hoshino.cti.Entity.Projectiles;

import cofh.core.init.CoreParticles;
import com.c2h6s.etshtinker.init.etshtinkerParticleType;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.c2h6s.etshtinker.util.vecCalc.*;

public class FallenStars extends ItemProjectile {
    public final Item environmental;

    public FallenStars(EntityType<? extends ItemProjectile> type, Level p_37249_, Item item) {
        super(type, p_37249_);
        this.environmental = item;
    }

    public float baseDamage;
    public int time = 0;
    public List<Entity> hitList = new ArrayList<>(List.of());


    @Override
    protected Item getDefaultItem() {
        return this.environmental;
    }


    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(this.environmental);
    }

    public void tick() {
        time++;
        Level world1 = this.getLevel();
        if (time >= 2 && world1 instanceof ServerLevel serverLevel) {
            Vec3 initVec = this.getDeltaMovement();
            initVec = initVec.scale(Math.max(0, 1 - 0.025 * time));
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
            if (getMold(vec3) >= 0.1) {
                if (this.environmental == CtiItem.star_frozen.get()) {
                    serverLevel.sendParticles(CoreParticles.FROST.get(), this.getX(), this.getY() + 0.5 * this.getBbHeight(), this.getZ(), 8, 0.25, 0.25, 0.25, 0.0125);
                }
                if (this.environmental == CtiItem.star_pressure.get()) {
                    serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5 * this.getBbHeight(), this.getZ(), 8, 0.25, 0.25, 0.25, 0.0125);
                }
                if (this.environmental == CtiItem.star_ionize.get()) {
                    serverLevel.sendParticles(etshtinkerParticleType.electric.get(), this.getX(), this.getY() + 0.5 * this.getBbHeight(), this.getZ(), 16, 0.3, 0.3, 0.3, 0.0125);
                }
                if (this.environmental == CtiItem.star_blaze.get()) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY() + 0.5 * this.getBbHeight(), this.getZ(), 8, 0.25, 0.25, 0.25, 0.0125);
                }
            }
            this.setDeltaMovement(vec3);
        }
        if (time > 300) {
            this.remove(RemovalReason.DISCARDED);
        }
        this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        List<LivingEntity> ls = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.25));
        if (!ls.isEmpty()) {
            ls.addAll(this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5)));
            for (LivingEntity living : ls) {
                if (living != null && living != this.getOwner() && !hitList.contains(living) && !(living instanceof Player)) {
                    if (this.getOwner() instanceof Player player) {
                        living.invulnerableTime = 0;
                        living.hurt(getSource(player, living), this.baseDamage);
                        if (world1 instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.EXPLOSION, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 1, 0, 0, 0, 0);
                            if (this.environmental == CtiItem.star_frozen.get()) {
                                serverLevel.sendParticles(CoreParticles.FROST.get(), living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 10, 0.05, 0.05, 0.05, 0.25);
                            }
                            if (this.environmental == CtiItem.star_pressure.get()) {
                                serverLevel.sendParticles(ParticleTypes.SMOKE, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 10, 0.05, 0.05, 0.05, 0.25);
                            }
                            if (this.environmental == CtiItem.star_ionize.get()) {
                                serverLevel.sendParticles(etshtinkerParticleType.electric.get(), living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 10, 0.05, 0.05, 0.05, 0.25);
                            }
                            if (this.environmental == CtiItem.star_blaze.get()) {
                                serverLevel.sendParticles(ParticleTypes.FLAME, living.getX(), living.getY() + 0.5 * living.getBbHeight(), living.getZ(), 10, 0.05, 0.05, 0.05, 0.25);
                            }
                        }
                    }
                    if (this.time < 299) {
                        this.time = 299;
                    }
                }
            }
            if (this.getOwner() instanceof Player player) {
                this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST_FAR);
            }
            hitList.addAll(ls);
        }
        super.tick();
    }


    public DamageSource getSource(Player player, @NotNull LivingEntity living) {
        CompoundTag nbt = living.getPersistentData();
        if (this.environmental == CtiItem.star_frozen.get()) {
            EnvironmentalHandler.addFrozenValue(living,100);
            return EDamageSource.indirectFrozen(false, player,10);
        }
        if (this.environmental == CtiItem.star_ionize.get()) {
            EnvironmentalHandler.addIonizeValue(living,100);
            return EDamageSource.indirectIonize(false, player,10);
        }
        if (this.environmental == CtiItem.star_pressure.get()) {
            EnvironmentalHandler.addPressureValue(living,100);
            return EDamageSource.indirectPressure(false, player,10);
        } else {
            EnvironmentalHandler.addScorchValue(living,100);
            return EDamageSource.indirectScorched(false, player,10);
        }
    }
}
