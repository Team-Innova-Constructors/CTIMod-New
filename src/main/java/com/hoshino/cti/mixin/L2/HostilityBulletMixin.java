package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2hostility.content.entity.BulletType;
import dev.xkmc.l2hostility.content.entity.HostilityBullet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HostilityBullet.class)
public abstract class HostilityBulletMixin extends ShulkerBullet {
    @Shadow(remap = false)
    private BulletType type;

    public HostilityBulletMixin(EntityType<? extends ShulkerBullet> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.isExplosion()) {
            return false;
        }
        if (type== BulletType.PLAIN) return super.hurt(pSource, pAmount);
        if (pSource.getDirectEntity() instanceof Projectile) {
            if (!this.level.isClientSide()) {
                ItemEntity shard = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(LCItems.EXPLOSION_SHARD.get()));
                this.level.addFreshEntity(shard);
            }
            this.discard();
            return true;
        }

        return super.hurt(pSource, pAmount);
    }
}
