package com.hoshino.cti.Entity.Projectiles;

import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.content.entityTicker.tickers.Fiery;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.AttackUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.List;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class MeleeFieryJavelinProjectile extends FieryJavelinProjectile{
    public MeleeFieryJavelinProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ToolStack tool;
    public List<Entity> hitList = new ArrayList<>();

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity instanceof LivingEntity&&entity != this.getOwner() && this.getOwner() instanceof Player player && !(entity instanceof Player)&&!this.hitList.contains(entity);
    }

    @Override
    protected void onHit(HitResult pResult) {
        if (pResult instanceof EntityHitResult entityHitResult){
            this.hitList.add(entityHitResult.getEntity());
            this.explode(entityHitResult.getEntity());
        }
    }
    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().scale(pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(this.getDeltaMovement().scale(0.75f));
        super.tick();
        if (this.tickCount>20){
            this.explode(this.position());
            this.discard();
        }
    }

    @Override
    public void explode(Entity target) {
        this.explode(target.getBoundingBox().getCenter());
    }
    public void explode(Vec3 position) {
        if (this.tool != null&&this.getOwner() instanceof Player player) {
            AABB aabb = new AABB(position.x - 1, position.y - 1, position.z - 1, position.x + 1, position.y + 1, position.z + 1);
            this.level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> entity != this.getOwner()).forEach(entity -> {
                inflictBurnt(player,entity,this.tool,5);
                EntityTickerManager.getInstancePlayerSpecific(entity,player.getUUID()).getOptional(CtiEntityTickers.FIERY.get()).ifPresent(entityTickerInstance ->
                        Fiery.burntDamage(entity,player,entityTickerInstance.level));
                if (player.hasEffect(CtiEffects.OVERHEAT.get())){
                    entity.invulnerableTime = 0;
                    AttackUtil.attackEntity(tool,player, InteractionHand.MAIN_HAND,entity,()->1,true, EquipmentSlot.MAINHAND,false,0,0.1f);
                }
            });
            this.playSound(SoundEvents.FIRECHARGE_USE);
            this.playSound(SoundEvents.FIREWORK_ROCKET_BLAST);
            if (this.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(CtiParticleType.FIERY_EXPLODE.get(), position.x, position.y, position.z, 1, 0, 0, 0, 0);
                serverLevel.sendParticles(CtiParticleType.RED_SPARK.get(), position.x, position.y, position.z, 16, 0, 0, 0, 0.4);
            }
        }
    }
}
