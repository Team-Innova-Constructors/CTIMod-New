package com.hoshino.cti.mixin.AetherMixin;

import com.aetherteam.aether.capability.AetherCapabilities;
import com.aetherteam.aether.capability.player.AetherPlayer;
import com.aetherteam.aether.entity.AetherBossMob;
import com.aetherteam.aether.entity.monster.dungeon.boss.SunSpirit;
import com.aetherteam.aether.event.AetherEventDispatch;
import com.aetherteam.nitrogen.entity.BossRoomTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = SunSpirit.class)
public abstract class SpiritMixin extends PathfinderMob implements AetherBossMob<SunSpirit>, Enemy, IEntityAdditionalSpawnData {
    @Shadow(remap = false) protected double velocity;

    @Shadow(remap = false) protected abstract void chatWithNearby(Component message);

    @Shadow(remap = false) public abstract void setBossFight(boolean isFighting);

    @Shadow(remap = false) @Nullable public abstract BossRoomTracker<SunSpirit> getDungeon();

    @Shadow public abstract boolean isInvulnerableTo(DamageSource source);

    @Shadow(remap = false) public abstract boolean isBossFight();

    protected SpiritMixin(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    private DamageSource cti_new$currentSource;
    @Unique
    private int cti$freezeTick;
    @Inject(method = "isInvulnerableTo",at = @At("HEAD"), cancellable = true)
    private void isInvulnerableTo(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if(cti$freezeTick >0){
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "tick",at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        cti$freezeTick--;
    }
    @Inject(method = "hurt", at = @At("HEAD"))
    private void onHurtHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        cti_new$currentSource =source;
        if (source.getMsgId().equals("aether.ice_crystal")) {
            if (source.getEntity() instanceof Player player) {
                LazyOptional<AetherPlayer> aetherPlayer = player.getCapability(AetherCapabilities.AETHER_PLAYER_CAPABILITY);
                if (!isBossFight()) {
                    this.chatWithNearby(Component.translatable("gui.aether.sun_spirit.line9").withStyle(ChatFormatting.GOLD));
                    this.setBossFight(true);
                    if (this.getDungeon() != null) {
                        this.closeRoom();
                    }
                    AetherEventDispatch.onBossFightStart(this, this.getDungeon());
                    aetherPlayer.ifPresent((cap) -> cap.setSeenSunSpiritDialogue(true));
                }
            }
            cti$freezeTick = 200;
        }
    }
    @Redirect(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean redirectSpawnMinion(Level instance, Entity entity) {
        if (cti_new$currentSource == null) return false;
        if (cti_new$currentSource.getMsgId().equals("aether.ice_crystal")) {
            return instance.addFreshEntity(entity);
        }
        return false;
    }

    @Inject(method = "hurt", at = @At("TAIL"))
    private void onHurtTail(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            SunSpirit sunSpirit = (SunSpirit) (Object) this;
            float healthPercentage = Math.max(0.1f, sunSpirit.getHealth() / sunSpirit.getMaxHealth());
            double targetVelocity = 1.5D - healthPercentage / 2;
            this.velocity = Mth.clamp(targetVelocity, 0.1D, 2.0D);
        }
    }

    @ModifyArg(method = "customServerAiStep",at = @At(value = "INVOKE", target = "Lcom/aetherteam/aether/entity/monster/dungeon/boss/SunSpirit;setFrozen(Z)V",remap = false))
    private boolean set(boolean frozen){
        return cti$freezeTick>0;
    }
    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructorTail(EntityType<? extends PathfinderMob> type, Level level, CallbackInfo ci) {
        SunSpirit sunSpirit = (SunSpirit) (Object) this;
        float healthPercentage = Math.max(0.1f, sunSpirit.getHealth() / sunSpirit.getMaxHealth());
        double targetVelocity = 1.5D -healthPercentage/2;
        this.velocity = Mth.clamp(targetVelocity, 0.1D, 2.0D);
    }

}
