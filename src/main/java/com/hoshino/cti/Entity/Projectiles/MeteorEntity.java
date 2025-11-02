package com.hoshino.cti.Entity.Projectiles;

import appeng.core.definitions.AEBlocks;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.util.DimensionConstants;
import com.mojang.math.Vector3f;
import earth.terrarium.ad_astra.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MeteorEntity extends Projectile {
    private static final EntityDataAccessor<Byte> EXPLOSION_POWER = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.BYTE);

    public MeteorEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    public MeteorEntity(Level level, double x, double y, double z, Vec3 movement) {
        this(CtiEntity.meteor_entity.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(movement);
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
        EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(this.level, this, this.position(), this.position().add(this.getDeltaMovement()), this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1), this::canHitEntity);
        super.move(MoverType.SELF, this.getDeltaMovement());
        if (entityhitresult != null && entityhitresult.getType() != HitResult.Type.MISS) {
            hitresult = entityhitresult;
        }
        if (this.level instanceof ServerLevel serverLevel) {
            DustParticleOptions options = new DustParticleOptions(new Vector3f(Vec3.fromRGB24(0x784C3D)), 1.0f);
            serverLevel.sendParticles(options, this.getX(), this.getY(), this.getZ(), 7, 0.25, 0.25, 0.25, 0.15);
            if (this.level.dimension() != DimensionConstants.MOON && this.level.dimension() != DimensionConstants.MERCURY) {
                serverLevel.sendParticles(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 4, 0.25, 0.25, 0.25, 0.15);
            }
        }
        if (hitresult.getType() != HitResult.Type.MISS) {
            this.onHit(hitresult);
        }
        if (this.onGround) {
            this.onHit(new BlockHitResult(this.position(), Direction.UP, this.blockPosition().below(), false));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        meteorExplode();
    }

    public void meteorExplode() {
        if (!this.level.isClientSide) {
            Explosion explosion = this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.getEntityData().get(EXPLOSION_POWER) * 0.1f, true, Explosion.BlockInteraction.DESTROY);
            List<BlockPos> list = explosion.getToBlow();
            List<Player> players = explosion.getHitPlayers().keySet().stream().toList();
            boolean generatedPress = false;
            for (BlockPos blockPos : list) {
                if ((this.level.getBlockState(blockPos).isAir() || this.level.getBlockState(blockPos).is(Blocks.FIRE) || !(this.level.getFluidState(blockPos).is(Fluids.EMPTY))) && this.level.getBlockState(blockPos.below()).isCollisionShapeFullBlock(this.level, blockPos)) {
                    int rnd = random.nextInt(20);
                    if (!generatedPress && random.nextInt(100) == 0) {
                        this.level.setBlockAndUpdate(blockPos, AEBlocks.MYSTERIOUS_CUBE.block().defaultBlockState());
                        generatedPress = true;
                    }
                    if (rnd == 0) {
                        this.level.setBlockAndUpdate(blockPos, CtiBlock.meteorite_ore.get().defaultBlockState());
                    } else if (rnd > 0 && rnd < 5) {
                        this.level.setBlockAndUpdate(blockPos, Blocks.MAGMA_BLOCK.defaultBlockState());
                    } else if (rnd > 5 && rnd < 9) {
                        this.level.setBlockAndUpdate(blockPos, AEBlocks.SKY_STONE_BLOCK.block().defaultBlockState());
                    } else if (rnd > 9 && rnd < 14) {
                        this.level.setBlockAndUpdate(blockPos, ModBlocks.SKY_STONE.get().defaultBlockState());
                    } else if (rnd == 15) {
                        this.level.setBlockAndUpdate(blockPos, AEBlocks.FLAWLESS_BUDDING_QUARTZ.block().defaultBlockState());
                    }
                }
            }
            for (Player player : players) {
                if (player != null) {
                    player.invulnerableTime = 0;
                    player.hurt(DamageSource.explosion(explosion), 70);
                }
            }
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        meteorExplode();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(EXPLOSION_POWER, (byte) 0);
    }

    public void setExplosionPower(byte power) {
        this.entityData.set(EXPLOSION_POWER, power);
    }
}
