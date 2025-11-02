package com.hoshino.cti.Entity.Projectiles;

import com.hoshino.cti.Modifier.StarDargonHit;
import com.hoshino.cti.client.particle.ParticleType.StarFallParticleType;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.register.CtiSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class StarDargonAmmo extends BaseFallenAmmo {
    public StarDargonAmmo(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public StarDargonAmmo(LivingEntity owner, Level level, BlockPos targetPosition, float damageAmount) {
        super(CtiEntity.star_dragon_ammo.get(), owner, level, targetPosition);
        this.setHurtDamage(damageAmount);
    }

    @Override
    protected void shockWaveHurt(Mob mob, Player player) {
        mob.hurt(DamageSource.playerAttack(player).bypassArmor().bypassMagic(),this.getHurtDamage());
        Vec3 knock = mob.position().subtract(getVec3TargetPosition()).normalize().scale(3);
        Vec3 finalKnock = new Vec3(knock.x(), 0.4, knock.z());
        mob.setDeltaMovement(finalKnock);
        if(mob.getHealth()<mob.getMaxHealth() * 0.21f||getHurtDamage()>mob.getMaxHealth() * 10f){
            mob.die(DamageSource.playerAttack(player));
            mob.discard();
            var data= ToolStack.from(player.getMainHandItem()).getPersistentData();
            var currentAmount=data.getInt(StarDargonHit.STAR_DUST);
            data.putInt(StarDargonHit.STAR_DUST,currentAmount + 1);
        }
    }

    @Override
    protected void onArrived(ServerPlayer player) {
        super.onArrived(player);
        this.directHurtLiving(DamageSource.playerAttack(player).bypassArmor().bypassMagic().bypassInvul(), this.getHurtDamage() * 10, 5);
        if (!this.level.isClientSide) {
            var particle = new StarFallParticleType(true, 1, 0xf8ffb2, 1, 1, 10, getVec3TargetPosition());
            player.getLevel().sendParticles(particle, getVec3TargetPosition().x(), getVec3TargetPosition().y() + 0.05, getVec3TargetPosition().z(), 1, 0, 0, 0, 0.25);
            getLevel().playSound(null, player, CtiSounds.starHit.get(), SoundSource.AMBIENT, 1f, 1);
        }
    }

    @Override
    protected @NotNull ParticleOptions getTailParticleType() {
        return ParticleTypes.SMOKE;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return new ItemStack(CtiItem.star_dragon_ammo.get());
    }
}
