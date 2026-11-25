package com.hoshino.cti.Entity.Projectiles;

import cofh.core.init.CoreMobEffects;
import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.AttackUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class FieryCircleSlashEntity extends Projectile {
    public IToolStackView tool =null;
    public List<Entity> hitList = new ArrayList<>();
    public int burntLevel = 5;
    public FieryCircleSlashEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
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
        if (tickCount>8){
            this.remove(RemovalReason.DISCARDED);
        }
        if (this.getOwner()!=null)
            this.setPos(this.getOwner().getX(),this.getOwner().getY()+0.5*this.getOwner().getBbHeight(),this.getOwner().getZ());
        super.tick();
        List<LivingEntity> ls =this.level.getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(0,8,0), living -> !hitList.contains(living));
        AtomicInteger elementalReleaseLv = new AtomicInteger(0);
        if (this.getOwner() instanceof Player player)
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
        for (LivingEntity entity :ls){
            if (entity!=null&&entity!=this.getOwner()&&this.getOwner() instanceof Player player&&!(entity instanceof Player)){
                if (tool!=null) {
                    entity.invulnerableTime = 0;
                    AttackUtil.attackEntity(tool,player, InteractionHand.MAIN_HAND,entity,()->1,false, EquipmentSlot.MAINHAND,false,0, GenreManager.calculateElementalRelease(elementalReleaseLv.get(),4,16));
                    entity.invulnerableTime =0;
                    entity.hurt(ElementalDamageSource.burnt(player), (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE)*tool.getModifierLevel(CtiModifiers.REPLACED_HEAT_SWORD.getId()));
                    entity.invulnerableTime = 0;
                    hitList.add(entity);
                }
            }
        }
    }
}
