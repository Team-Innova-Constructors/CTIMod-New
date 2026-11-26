package com.hoshino.cti.Entity.Projectiles;

import blusunrize.immersiveengineering.common.util.IESounds;
import com.c2h6s.etshtinker.Entities.PlasmaSlashEntity;
import com.c2h6s.etshtinker.etshtinker;
import com.c2h6s.etshtinker.init.EtshtinkerModifiers;
import com.c2h6s.etshtinker.init.etshtinkerHook;
import com.c2h6s.etshtinker.util.attackUtil;
import com.hoshino.cti.client.CtiParticleType;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;

import static com.c2h6s.etshtinker.etshtinker.EtSHrnd;

public class PlasmaCircleSlashEntity extends PlasmaSlashEntity {
    public PlasmaCircleSlashEntity(EntityType<? extends Projectile> type, Level p_37249_) {
        super(type, p_37249_);
        this.followUser = false;
        this.angle*=2f;
    }
    public PlasmaCircleSlashEntity(Level level){
        this(CtiEntity.PLASMA_CIRCLE_SLASH.get(),level);
    }
    public PlasmaCircleSlashEntity(Level level,int slash){
        this(CtiEntity.PLASMA_CIRCLE_SLASH.get(),level);
        this.setSlashType(slash);
    }


    @Override
    public AABB getAttackRange() {
        float scale = getScale();
        if (this.getEchoCount()!=1)
            scale*=0.4f;
        return new AABB(this.getX() -2-scale,this.getY()-2-scale,this.getZ()-2-scale,this.getX()+2+scale,this.getY()+2+scale,this.getZ()+2+scale);
    }


    @Override
    public void tick() {
        Vec3 rayVec3 =this.getDeltaMovement();

        if (this.firstTick) {
            double d0 = rayVec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(rayVec3.x, rayVec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(rayVec3.y, d0) * (double)(180F / (float)Math.PI)));
            if (!this.isSwiftEcho&&!this.isEcho&&this.level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(CtiParticleType.NORMAL_SLASH.get(),this.getX(),this.getY(),this.getZ(),1,0,0,0,1);
            this.isCritical = EtSHrnd().nextFloat(0, 1) <= this.CriticalRate;
            if (this.isCritical) {
                this.entityData.set(KEY_IS_CRITICAL, true);
                this.setScale(this.getScale()*1.5f);
                this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1.25f,1.25f);
                this.echoTriggerChance += 0.5f;
            }
            else this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP,1,1);
            if (this.isEcho&&this.getEchoCount()==1&&this.level instanceof ServerLevel serverLevel){
                serverLevel.playSound(null,this, IESounds.spark.get(), SoundSource.PLAYERS,1f,1f);
            }
        }

        if (this.tool==null&&this.getOwner() instanceof Player player){
            this.tool= ToolStack.from( player.getMainHandItem());
        }
        super.baseTick();
        if (this.tickCount>=5){
            this.onSlashLifeEnds();
            return;
        }
        if (this.tickCount<=1){
            return;
        }
        Entity entity =this.getOwner();
        if (entity==null){
            return;
        }
        if (entity instanceof Player player) {
            AABB aabb = this.getAttackRange();
            List<Entity> ls0 = this.level.getEntitiesOfClass(Entity.class, aabb,this::canHitEntity);
            float overCrit =Math.max( CriticalRate -1,0);
            boolean isCritical = this.isCritical;
            if (this.getEchoCount()==1) {
                this.isCritical = true;
                if (overCrit<=0)
                    overCrit+=this.CriticalRate+1;
                else overCrit+=2;
            }
            this.hitRemain=16;
            if (this.tickCount==3){
                if (this.getEchoCount()==1&&this.tool!=null&&this.getOwner() instanceof ServerPlayer serverPlayer&&this.tool.getModifierLevel(EtshtinkerModifiers.WAVE_REFRACTION.getId())>0){
                    var strength = serverPlayer.attackStrengthTicker;
                    serverPlayer.attackStrengthTicker = Integer.MAX_VALUE;
                    LeftClickModifierHook.handleLeftClick(serverPlayer.getMainHandItem(),serverPlayer, EquipmentSlot.MAINHAND);
                    serverPlayer.attackStrengthTicker = strength;
                }
            }
            for (Entity targets : ls0) {
                if (targets instanceof LivingEntity living&&this.tool!=null) {
                    for (ModifierEntry modifier : this.tool.getModifierList()) {
                        modifier.getHook(etshtinkerHook.BEFORE_SLASH_HIT).beforePlasmaSlashHit(this.tool, living, this, this.isCritical);
                    }
                    targets.invulnerableTime = 0;
                    if (!isEcho||EtSHrnd().nextFloat()<=echoTriggerChance) {
                        float damage = this.damage;
                        if (this.getEchoCount()!=1) damage*=0.75f;
                        else damage*=1.25f;
                        float attackDamage = damage;
                        ToolAttackContext context = new ToolAttackContext(player, player, InteractionHand.MAIN_HAND, living, living, this.isCritical, 1.0F, true);
                        if (etshtinker.EtSHrnd().nextFloat() <= this.echoTriggerChance - 1.0F && this.isEcho) {
                            for(ModifierEntry entry : this.tool.getModifierList()) {
                                attackDamage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(this.tool, entry, context, this.damage, attackDamage);
                            }
                        }
                        attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, targets, () -> (double)1.0F, true, Util.getSlotType(InteractionHand.MAIN_HAND), attackDamage, this.isCritical, true, true, true, overCrit);
                    }
                    else {
                        float criticalModifier = this.isCritical ? 1.5f+overCrit : 1.0f;
                        CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(player, living, this.isCritical, criticalModifier);
                        this.isCritical = hitResult != null;
                        if (this.isCritical) {
                            criticalModifier = hitResult.getDamageModifier();
                        }
                        targets.hurt(DamageSource.playerAttack(player),this.damage*criticalModifier);
                    }
                    targets.invulnerableTime=0;
                    for (ModifierEntry modifier : this.tool.getModifierList()) {
                        modifier.getHook(etshtinkerHook.AFTER_SLASH_HIT).afterPlasmaSlashHit(this.tool, living, this, this.isCritical, this.damage);
                    }
                    hitList.add(targets);
                    this.hitRemain--;
                    if (this.hitRemain <= 0) {
                        break;
                    }
                }else if (targets != null && !hitList.contains(targets)){
                    targets.invulnerableTime = 0;
                    attackUtil.attackEntity(this.tool, player, InteractionHand.MAIN_HAND, targets, ()->1, true, Util.getSlotType(InteractionHand.MAIN_HAND), this.damage, false, true, true, true,overCrit);
                    targets.invulnerableTime=0;
                }
            }
            this.isCritical = isCritical;
        }
    }

    @Override
    public String getTexturePath() {
        return "textures/projectile/plasma_slash/plasma_circle_slash_powered_" + this.getSlashType() + "_";
    }
    public void onSlashLifeEnds(){
        if (tryEchoSlash()) return;
        if (getEcho()<=0&&this.fluidStack!=null){
            swiftEcho();
        }
        this.discard();
    }
    @Override
    public boolean tryEchoSlash() {
        boolean b = super.tryEchoSlash();
        if (b&&this.level instanceof ServerLevel serverLevel){
            if (this.getEchoCount()!=1)
                serverLevel.sendParticles(CtiParticleType.NORMAL_SLASH.get(),this.getX(),this.getY(),this.getZ(),1,0,0,0,1);
            else serverLevel.playSound(null,this, IESounds.spark.get(), SoundSource.PLAYERS,1f,1f);
        }
        return b;
    }

    public void swiftEcho(){
        if (!this.isSwiftEcho&&tool!=null&&tool.getModifierLevel(EtshtinkerModifiers.SWIFT_ECHO.getId())>0&&this.getOwner() instanceof ServerPlayer player){
            PlasmaSlashEntity slash =new PlasmaCircleSlashEntity(level,this.getSlashType());
            slash.damage=this.damage;
            slash.setOwner(player);
            slash.setDeltaMovement(this.getDeltaMovement());
            slash.setScale(this.getScale());
            slash.setToolstack(this.tool);
            slash.fuelUsed = this.fuelUsed;
            slash.CriticalRate=this.CriticalRate;
            slash.echoTriggerChance = this.echoTriggerChance;
            slash.setPos(this.position());
            for (ModifierEntry modifier : tool.getModifierList()) {
                slash = modifier.getHook(etshtinkerHook.SLASH_CREATE).plasmaSlashCreate(tool, fluidStack, player, slash);
            }
            slash.setEcho(this.getEcho());
            slash.setEchoCount(this.getEchoCount());
            slash.isEcho=this.isEcho;
            slash.isCritical=this.isCritical;
            slash.isSwiftEcho = true;
            level.addFreshEntity(slash);
        }
    }

}
