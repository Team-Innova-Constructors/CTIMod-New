package com.hoshino.cti.Entity.Projectiles;

import cofh.core.init.CoreMobEffects;
import com.hoshino.cti.util.AttackUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class FierySlashProjectile extends Projectile {
    public IToolStackView tool =null;
    public List<Entity> hitList = new ArrayList<>();
    public int burntLevel = 5;
    public FierySlashProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        Vec3 movement =this.getDeltaMovement();
        if (this.firstTick) {
            double d0 = movement.horizontalDistance();
            this.setYRot((float) (Mth.atan2(movement.x, movement.z) * (double) (180F / (float) Math.PI)));
            this.setXRot((float) (Mth.atan2(movement.y, d0) * (double) (180F / (float) Math.PI)));
        }
        movement = movement.scale(0.7f);
        this.setDeltaMovement(movement);
        if (tickCount>=10){
            this.remove(RemovalReason.DISCARDED);
        }
        this.setPos(movement.x+this.getX(),movement.y+this.getY(),movement.z+this.getZ());
        super.tick();
        List<LivingEntity> ls =this.level.getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().expandTowards(movement), living -> !hitList.contains(living));
        for (LivingEntity entity :ls){
            if (entity!=null&&entity!=this.getOwner()&&this.getOwner() instanceof Player player&&!(entity instanceof Player)){
                if (tool!=null) {
                    inflictBurnt(player,entity,tool,this.burntLevel);
                    entity.invulnerableTime = 0;
                    AttackUtil.attackEntity(tool,player, InteractionHand.MAIN_HAND,entity,()->1,false, EquipmentSlot.MAINHAND,false,0,1f);
                    entity.invulnerableTime =0;
                    hitList.add(entity);
                }
                entity.forceAddEffect(new MobEffectInstance(CoreMobEffects.SHOCKED.get(),1000,3,false,false),player);
            }
        }
    }
}
