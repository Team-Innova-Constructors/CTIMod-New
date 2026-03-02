package com.hoshino.cti.Entity.Projectiles.base;

import com.hoshino.cti.util.EntityInRangeUtil;
import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.util.ProjectileUtil;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import lombok.Getter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.helper.ModifierLootingHandler;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.ArrayList;
import java.util.List;

import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getAttributeAttackDamage;
import static slimeknights.tconstruct.library.tools.helper.ToolAttackUtil.getLivingEntity;

public class BasicFlyingSwordEntity extends Projectile{
    public static final EntityDataAccessor<Integer> KEY_HOMING_ENTITY_ID = SynchedEntityData.defineId(BasicFlyingSwordEntity.class, EntityDataSerializers.INT);
    public BasicFlyingSwordEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.initialRotation = random.nextFloat()*180;
    }
    public float damagePercentage = 0.25f;
    public ToolStack tool = null;
    public IntOpenHashSet piercedEntities = new IntOpenHashSet();
    public float initialRotation;
    @Getter
    public List<Vec3> positionCache = new ArrayList<>();
    public List<Vec3> rotationCache = new ArrayList<>();
    public int lifeTime = 200;
    public boolean isTrueHurt = false;

    @Override
    protected void defineSynchedData() {
        this.entityData.define(KEY_HOMING_ENTITY_ID,-1);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public @Nullable LivingEntity getHomingEntity(){
        int id = this.entityData.get(KEY_HOMING_ENTITY_ID);
        if (id<0) return null;
        return this.level.getEntity(id) instanceof LivingEntity living?living:null;
    }
    public void setHomingEntity(@Nullable LivingEntity living){
        if (living==null){
            this.entityData.set(KEY_HOMING_ENTITY_ID,-1);
            return;
        }
        this.entityData.set(KEY_HOMING_ENTITY_ID,living.getId());
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount>this.lifeTime) this.discard();
        if (this.shouldFindHitEntity()&&!this.isRemoved()&&this.getOwner() instanceof LivingEntity living&&this.tool!=null){
            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(1).expandTowards(this.getDeltaMovement()),this::canHitEntity);
            entities.forEach(entity -> {
                entity.invulnerableTime=0;
                this.attackEntity(this.tool,living,InteractionHand.MAIN_HAND,entity,EquipmentSlot.MAINHAND,false,0,this.isCritical());
                entity.invulnerableTime=0;
                this.piercedEntities.add(entity.getId());
                if (this.getHomingEntity()==entity) this.setHomingEntity(null);
                if (this.shouldDiscardAfterHit(entity)) this.discard();
            });
        }
        if (!this.isRemoved()) {
            if (!this.level.isClientSide&&shouldHoming()) {
                if (this.getHomingEntity() != null) {
                    if (!this.getHomingEntity().isAlive()) this.setHomingEntity(null);
                }
                if (this.tickCount > 5) {
                    if (this.getHomingEntity() == null)
                        this.setHomingEntity(EntityInRangeUtil.getNearestLivingEntity(this, 16, this.piercedEntities, entity -> this.canHitEntity(entity) && !(entity instanceof Player)));
                    if (this.getHomingEntity() != null) ProjectileUtil.homingToward(this, this.getHomingEntity(), 1, 8);
                }
            }
            this.setPos(this.position().add(this.getDeltaMovement()));
            Vec3 vec3 = this.getDeltaMovement().normalize();
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            if (this.getParticle()!=null){
                var particle = this.getParticle();
                Vec3 movement = this.getDeltaMovement();
                Vec3 direction = movement.reverse().normalize();
                for (double d =0;d<movement.length();d+=0.2){
                    Vec3 pos = this.position().add(direction.scale(d));
                    this.level.addParticle(particle,pos.x+random.nextFloat()*0.02,pos.y+random.nextFloat()*0.02,pos.z+random.nextFloat()*0.02,random.nextFloat()*0.02,random.nextFloat()*0.02,random.nextFloat()*0.02);
                }
            }else if (this.level instanceof ServerLevel serverLevel){
                this.spawnParticle(serverLevel);
            }
        }
    }

    public final boolean attackEntity(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand,
                                       Entity targetEntity, EquipmentSlot sourceSlot, boolean setDamage, float damageSet,boolean critical) {
        if (tool.isBroken()) {
            return false;
        }
        Level level = attackerLiving.level;
        if (level.isClientSide) {
            return true;
        }
        LivingEntity targetLiving = getLivingEntity(targetEntity);
        Player attackerPlayer = null;
        if (attackerLiving instanceof Player player) {
            attackerPlayer = player;
        }

        float damage = getAttributeAttackDamage(tool, attackerLiving, sourceSlot);
        if (setDamage) damage = damageSet;

        boolean isCritical = critical;


        ToolAttackContext context = new ToolAttackContext(attackerLiving, attackerPlayer, hand, sourceSlot, targetEntity, targetLiving, isCritical, 1, true);

        float baseDamage = damage;
        List<ModifierEntry> modifiers = tool.getModifierList();
        for (ModifierEntry entry : modifiers) {
            damage = entry.getHook(ModifierHooks.MELEE_DAMAGE).getMeleeDamage(tool, entry, context, baseDamage, damage);
        }

        if (damage <= 0) {
            return false;
        }

        float knockback = (float) attackerLiving.getAttributeValue(Attributes.ATTACK_KNOCKBACK) / 2f;
        if (targetLiving != null) {
            knockback += 0.4f;
        }
        SoundEvent sound;
        if (attackerLiving.isSprinting()) {
            sound = SoundEvents.PLAYER_ATTACK_KNOCKBACK;
            knockback += 0.5f;
        } else {
            sound = SoundEvents.PLAYER_ATTACK_STRONG;
        }


        float criticalModifier = isCritical ? 1.5f : 1.0f;
        if (attackerPlayer != null) {
            CriticalHitEvent hitResult = ForgeHooks.getCriticalHit(attackerPlayer, targetEntity, isCritical, isCritical ? 1.5F : 1.0F);
            isCritical = hitResult != null;
            if (isCritical) {
                criticalModifier = hitResult.getDamageModifier();
            }
        }
        if (isCritical) {
            damage *= criticalModifier;
        }

        damage*=damagePercentage;
        damage = this.getDamage(damage);

        boolean isMagic = damage > baseDamage;

        float oldHealth = 0.0F;
        if (targetLiving != null) {
            oldHealth = targetLiving.getHealth();
        }

        float baseKnockback = knockback;
        for (ModifierEntry entry : modifiers) {
            knockback = entry.getHook(ModifierHooks.MELEE_HIT).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }

        ModifierLootingHandler.setLootingSlot(attackerLiving, sourceSlot);
        DamageSource source = this.createDamageSource();


        boolean didHit = isTrueHurt&&targetLiving!=null? ((ILivingEntityMixin)targetLiving).cti$strictHurt(source,damage) : targetEntity.hurt(source,damage);

        this.doAfterHitEffect(targetEntity,damage);


        ModifierLootingHandler.setLootingSlot(attackerLiving, EquipmentSlot.MAINHAND);

        if (!didHit) {
            level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, attackerLiving.getSoundSource(), 1.0F, 1.0F);

            for (ModifierEntry entry : modifiers) {
                entry.getHook(ModifierHooks.MELEE_HIT).failedMeleeHit(tool, entry, context, damage);
            }

            return false;
        }

        float damageDealt = damage;
        if (targetLiving != null) {
            damageDealt = oldHealth - targetLiving.getHealth();
        }

        if (targetEntity.hurtMarked && targetEntity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
            targetEntity.hurtMarked = false;
        }

        if (attackerPlayer != null) {
            if (isCritical) {
                sound = SoundEvents.PLAYER_ATTACK_CRIT;
                attackerPlayer.crit(targetEntity);
            }
            if (isMagic) {
                attackerPlayer.magicCrit(targetEntity);
            }
            level.playSound(null, attackerLiving.getX(), attackerLiving.getY(), attackerLiving.getZ(), sound, attackerLiving.getSoundSource(), 1.0F, 1.0F);
        }
        if (damageDealt > 2.0F && level instanceof ServerLevel server) {
            int particleCount = (int)(damageDealt * 0.5f);
            server.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(), particleCount, 0.1, 0, 0.1, 0.2);
        }
        attackerLiving.setLastHurtMob(targetEntity);
        if (targetLiving != null) {
            EnchantmentHelper.doPostHurtEffects(targetLiving, attackerLiving);
        }
        for (ModifierEntry entry : modifiers) {
            entry.getHook(ModifierHooks.MELEE_HIT).afterMeleeHit(tool, entry, context, damageDealt);
        }

        if (attackerPlayer != null) {
            if (targetLiving != null) {
                attackerPlayer.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
            }
        }

        return true;
    }

    public boolean isCritical(){return false;}
    public void doAfterHitEffect(@NotNull Entity target,float damageDealt){}
    public DamageSource createDamageSource(){
        return new IndirectEntityDamageSource("elemental_sword_"+this.getId(),this,this.getOwner());
    }
    public float getDamage(float damage){return damage;}
    public @Nullable ParticleOptions getParticle(){return null;}
    public boolean shouldDiscardAfterHit(Entity entity){return entity instanceof LivingEntity;}
    public void spawnParticle(ServerLevel serverLevel){}
    public boolean shouldFindHitEntity(){return true;}
    public boolean shouldHoming(){return true;}

    @Override
    protected boolean canHitEntity(@NotNull Entity pTarget) {
        if (!pTarget.isAlive()) return false;
        if (pTarget==this.getOwner()||pTarget instanceof Projectile||this.piercedEntities.contains(pTarget.getId())) return false;
        if (pTarget instanceof ItemEntity||pTarget instanceof ExperienceOrb) return false;
        return !(pTarget instanceof Player player) || !(this.getOwner() instanceof Player player1) || player1.canHarmPlayer(player);
    }

}
