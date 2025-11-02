package com.hoshino.cti.mixin;

import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import com.hoshino.cti.content.environmentSystem.IEnvironmentalSource;
import com.hoshino.cti.mixin.TIMixin.ServerPlayerAccessor;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.ILivingEntityMixin;
import com.hoshino.cti.util.StrictDamageProcess;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.common.ForgeHooks;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import java.util.Optional;

import static com.hoshino.cti.content.environmentSystem.EnvironmentalHandler.*;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityMixin {
    @Shadow
    public abstract boolean hasEffect(MobEffect p_21024_);

    @Unique
    private static final Logger cti$LOGGER = LogUtils.getLogger();

    @Shadow
    @Nullable
    public abstract LivingEntity getLastHurtByMob();

    @Inject(at = @At(value = "HEAD"), method = "checkTotemDeathProtection", cancellable = true)
    private void checkTotemDeathProtection(DamageSource pDamageSource, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (pDamageSource.isBypassMagic() || pDamageSource.getEntity() == living) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "aiStep", cancellable = true)
    public void StopAi(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getPersistentData().getInt("emp") > 0) {
            entity.getPersistentData().putInt("emp", entity.getPersistentData().getInt("emp") - 1);
            if (entity.getPersistentData().getInt("emp") <= 0) {
                entity.getPersistentData().remove("emp");
            }
            ci.cancel();
        }
    }


    @Inject(at = @At(value = "HEAD"), method = "tick")
    public void CtiTick(CallbackInfo callbackInfo) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        Level level = entity.level;
        if (entity instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return;
        }
        if (!(entity instanceof Player) && entity != null && entity.getPersistentData().getBoolean("vulnerable")) {
            entity.invulnerableTime = 0;
        }
        if (!level.isClientSide) {
            if (level.getGameTime() % 10 == 0) {
                EnvironmentalHandler.livingTick(entity);
            }
        }
    }

    @Inject(at = @At(value = "TAIL"), method = "tick")
    public void FreezeTick(CallbackInfo callbackInfo) {
        LivingEntity entity = ((LivingEntity) (Object) this);
        Level level = entity.level;
        if (!level.isClientSide) {
            if (getFrozenResistance(entity) > 0.5 && entity instanceof Player) {
                entity.setIsInPowderSnow(false);
                entity.setTicksFrozen(-10);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "hurt", cancellable = true)
    public void Hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(CtiEffects.ev.get())) {
            cir.setReturnValue(false);
        }else if (source instanceof IEnvironmentalSource){
            cir.setReturnValue(EnvironmentalHandler.hurtEntity((LivingEntity) (Object) this,source,amount));
        }
    }

    @Unique
    public void cti$actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        LivingEntity living =(LivingEntity) (Object) this;
        pDamageAmount = StrictDamageProcess.getStrictDamageForEntity(living,pDamageAmount);
        ForgeHooks.onLivingHurt(living, pDamageSource, pDamageAmount);
        if (pDamageAmount > 0.0F && pDamageAmount < 3.4028235E37F && pDamageSource.getEntity() instanceof ServerPlayer) {
            ((ServerPlayer) pDamageSource.getEntity()).awardStat(Stats.CUSTOM.get(Stats.DAMAGE_DEALT_ABSORBED), Math.round(pDamageAmount * 10.0F));
        }

        ForgeHooks.onLivingDamage(living, pDamageSource, pDamageAmount);

        float f1 = living.getHealth();
        living.getCombatTracker().recordDamage(pDamageSource, f1, pDamageAmount);
        living.setHealth(f1 - pDamageAmount);
        living.gameEvent(GameEvent.ENTITY_DAMAGE);
    }

    @Unique
    public boolean cti$strictHurt(DamageSource pSource, float pAmount){
        LivingEntity living = (LivingEntity)(Object) this;
        LivingEntityAccessor accessor = (LivingEntityAccessor) living;
        ForgeHooks.onLivingAttack(living, pSource, pAmount);
        if (living.level.isClientSide) {
            return false;
        } else if (living.isDeadOrDying()) {
            return false;
        } else {
            if (living.isSleeping() && !living.level.isClientSide) {
                living.stopSleeping();
            }

            accessor.setNoActionTime(0);
            float f = pAmount;
            boolean flag = false;
            float f1 = 0.0F;
            living.animationSpeed = 1.5F;
            boolean flag1 = true;
            if ((float)living.invulnerableTime > 10.0F) {
                if (pAmount <= accessor.getLastHurt()) {
                    return false;
                }

                this.cti$actuallyHurt(pSource, pAmount - accessor.getLastHurt());
                accessor.setLastHurt(pAmount);
                flag1 = false;
            } else {
                accessor.setLastHurt(pAmount);
                living.invulnerableTime = 20;
                this.cti$actuallyHurt(pSource, pAmount);
                living.hurtDuration = 10;
                living.hurtTime = living.hurtDuration;
            }

            if (pSource.isDamageHelmet() && !living.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                accessor.hurtHelmet(pSource, pAmount);
                pAmount *= 0.75F;
            }

            living.hurtDir = 0.0F;
            Entity entity1 = pSource.getEntity();
            if (entity1 != null) {
                if (entity1 instanceof LivingEntity && !pSource.isNoAggro()) {
                    living.setLastHurtByMob((LivingEntity)entity1);
                }

                if (entity1 instanceof Player) {
                    accessor.setLastHurtByPlayerTime(100);
                    living.setLastHurtByPlayer((Player)entity1);
                } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
                    if (tamableEntity.isTame()) {
                        accessor.setLastHurtByPlayerTime(100);
                        LivingEntity livingentity = tamableEntity.getOwner();
                        if (livingentity != null && livingentity.getType() == EntityType.PLAYER) {
                            living.setLastHurtByPlayer((Player)livingentity);
                        } else {
                            living.setLastHurtByPlayer(null);
                        }
                    }
                }
            }

            if (flag1) {
                if (pSource instanceof EntityDamageSource && ((EntityDamageSource)pSource).isThorns()) {
                    living.level.broadcastEntityEvent(living, (byte)33);
                } else {
                    byte b0;
                    if (pSource == DamageSource.DROWN) {
                        b0 = 36;
                    } else if (pSource.isFire()) {
                        b0 = 37;
                    } else if (pSource == DamageSource.SWEET_BERRY_BUSH) {
                        b0 = 44;
                    } else if (pSource == DamageSource.FREEZE) {
                        b0 = 57;
                    } else {
                        b0 = 2;
                    }

                    living.level.broadcastEntityEvent(living, b0);
                }

                if (pSource != DamageSource.DROWN) {
                    accessor.markHurt();
                }

                if (entity1 != null) {
                    double d1 = entity1.getX() - living.getX();

                    double d0;
                    for(d0 = entity1.getZ() - living.getZ(); d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D) {
                        d1 = (Math.random() - Math.random()) * 0.01D;
                    }

                    living.hurtDir = (float)(Mth.atan2(d0, d1) * (double)(180F / (float)Math.PI) - (double)living.getYRot());
                    living.knockback(0.4F, d1, d0);
                } else {
                    living.hurtDir = (float)((int)(Math.random() * 2.0D) * 180);
                }
            }

            if (living.isDeadOrDying()) {
                SoundEvent soundevent = accessor.getDeathSound();
                if (flag1 && soundevent != null) {
                    living.playSound(soundevent, accessor.getSoundVolume(), living.getVoicePitch());
                }
                this.cti$strictDie(pSource);
            } else if (flag1) {
                accessor.playHurtSound(pSource);
            }

            accessor.setLastDamageSource(pSource);
            accessor.setLastDamageStamp(living.level.getGameTime());

            if (living instanceof ServerPlayer) {
                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)living, pSource, f, pAmount, flag);
            }

            if (entity1 instanceof ServerPlayer) {
                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)entity1, living, pSource, f, pAmount, flag);
            }

            return true;
        }
    }
    @Unique
    public void cti$strictDie(DamageSource pDamageSource) {
        LivingEntity living = (LivingEntity)(Object) this;
        LivingEntityAccessor accessor = (LivingEntityAccessor) living;
        if (living instanceof ServerPlayer player){
            ServerPlayerAccessor playerAccessor = (ServerPlayerAccessor) player;
            living.gameEvent(GameEvent.ENTITY_DIE);
            boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES);
            if (flag) {
                Component component = player.getCombatTracker().getDeathMessage();
                player.connection.send(new ClientboundPlayerCombatKillPacket(player.getCombatTracker(), component), PacketSendListener.exceptionallySend(() -> {
                    int i = 256;
                    String s = component.getString(256);
                    Component component1 = Component.translatable("death.attack.message_too_long", Component.literal(s).withStyle(ChatFormatting.YELLOW));
                    Component component2 = Component.translatable("death.attack.even_more_magic", player.getDisplayName()).withStyle((p_143420_) -> {
                        return p_143420_.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component1));
                    });
                    return new ClientboundPlayerCombatKillPacket(player.getCombatTracker(), component2);
                }));
                Team team = player.getTeam();
                if (team != null && team.getDeathMessageVisibility() != Team.Visibility.ALWAYS) {
                    if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OTHER_TEAMS) {
                        player.server.getPlayerList().broadcastSystemToTeam(player, component);
                    } else if (team.getDeathMessageVisibility() == Team.Visibility.HIDE_FOR_OWN_TEAM) {
                        player.server.getPlayerList().broadcastSystemToAllExceptTeam(player, component);
                    }
                } else {
                    player.server.getPlayerList().broadcastSystemMessage(component, false);
                }
            } else {
                player.connection.send(new ClientboundPlayerCombatKillPacket(player.getCombatTracker(), CommonComponents.EMPTY));
            }

            playerAccessor.removeEntitiesOnShoulder();
            if (player.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
                playerAccessor.tellNeutralMobsThatIDied();
            }

            if (!player.isSpectator()) {
                accessor.dropAllDeathLoot(pDamageSource);
            }

            player.getScoreboard().forAllObjectives(ObjectiveCriteria.DEATH_COUNT, player.getScoreboardName(), Score::increment);
            LivingEntity livingentity = player.getKillCredit();
            if (livingentity != null) {
                player.awardStat(Stats.ENTITY_KILLED_BY.get(livingentity.getType()));
                livingentity.awardKillScore(player, accessor.getDeathScore(), pDamageSource);
                accessor.createWitherRose(livingentity);
            }

            player.level.broadcastEntityEvent(player, (byte)3);
            player.awardStat(Stats.DEATHS);
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
            player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
            player.clearFire();
            player.setTicksFrozen(0);
            player.setSharedFlagOnFire(false);
            player.getCombatTracker().recheckStatus();
            player.setLastDeathLocation(Optional.of(GlobalPos.of(player.level.dimension(), player.blockPosition())));
        }else {
            living.setHealth(0);
            if (!living.isRemoved() && !accessor.getDead()) {
                Entity entity = pDamageSource.getEntity();
                LivingEntity livingentity = living.getKillCredit();
                if (accessor.getDeathScore() >= 0 && livingentity != null) {
                    livingentity.awardKillScore(living, accessor.getDeathScore(), pDamageSource);
                }

                if (living.isSleeping()) {
                    living.stopSleeping();
                }

                if (!living.level.isClientSide && living.hasCustomName()) {
                    cti$LOGGER.info("Named entity {} died: {}", living, living.getCombatTracker().getDeathMessage().getString());
                }

                accessor.setDead(true);
                living.getCombatTracker().recheckStatus();
                if (living.level instanceof ServerLevel) {
                    if (entity == null || entity.wasKilled((ServerLevel) living.level, living)) {
                        living.gameEvent(GameEvent.ENTITY_DIE);
                        accessor.dropAllDeathLoot(pDamageSource);
                        accessor.createWitherRose(livingentity);
                    }

                    living.level.broadcastEntityEvent(living, (byte) 3);
                }

                living.setPose(Pose.DYING);
            }
            if (living instanceof Player player) {
                accessor.reapplyPosition();
                if (!player.isSpectator()) {
                    accessor.dropAllDeathLoot(pDamageSource);
                }

                if (pDamageSource != null) {
                    player.setDeltaMovement((double) (-Mth.cos((player.hurtDir + player.getYRot()) * ((float) Math.PI / 180F)) * 0.1F), (double) 0.1F, (double) (-Mth.sin((player.hurtDir + player.getYRot()) * ((float) Math.PI / 180F)) * 0.1F));
                } else {
                    player.setDeltaMovement(0.0D, 0.1D, 0.0D);
                }

                player.awardStat(Stats.DEATHS);
                player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
                player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
                player.clearFire();
                player.setSharedFlagOnFire(false);
                player.setLastDeathLocation(Optional.of(GlobalPos.of(player.level.dimension(), player.blockPosition())));
            }
        }
    }
}
