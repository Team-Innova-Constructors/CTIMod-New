package com.hoshino.cti.mixin.Dummy;

import dev.xkmc.l2complements.init.registrate.LCItems;
import net.mehvahdjukaar.dummmmmmy.common.DamageType;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.dummmmmmy.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(TargetDummyEntity.class)
public abstract class TargetDummyMixin extends Mob {
    @Shadow private int lastTickActuallyDamaged;

    @Shadow private DamageSource currentDamageSource;

    @Shadow protected abstract void showDamageDealt(float damage, DamageType type);

    @Shadow private boolean critical;

    protected TargetDummyMixin(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;displayClientMessage(Lnet/minecraft/network/chat/Component;Z)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void summon(CallbackInfo ci, BlockPos onPos, BlockState onState, CombatTracker tracker, float combatDuration, CommonConfigs.DpsMode dpsMode, boolean dynamic, float seconds, float dps, List outOfCombat, Iterator var10, Map.Entry e, ServerPlayer p, int timer, boolean showMessage){
        if (this.lastHurtByPlayer != null && dps > 35000 && this.lastHurtByPlayer.distanceTo(this) < 3) {
            ItemEntity shard = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(LCItems.SPACE_SHARD.get()));
            this.level.addFreshEntity(shard);
        }
    }
    @Inject(method = "tick",at = @At(value = "HEAD"))
    private void healAtStart(CallbackInfo ci){
        this.setHealth(this.getMaxHealth());
    }


    /**
     * @author EtSH_C2H6S
     * @reason 让假人能触发匠魂的afterMeleeHit
     */
    @Overwrite
    public void setHealth(float newHealth) {
        if (newHealth >= this.getHealth()) {
            this.entityData.set(DATA_HEALTH_ID, newHealth);
        } else {
            float damage = this.getHealth() - newHealth;
            if (damage > 0.0F) {
                if (this.lastTickActuallyDamaged != this.tickCount) {
                    this.animationPosition = 0.0F;
                }

                this.animationPosition = Math.min(this.animationPosition + damage, 60.0F);
                this.lastTickActuallyDamaged = this.tickCount;
                if (!this.level.isClientSide) {
                    DamageSource actualSource = null;
                    CombatEntry currentCombatEntry = this.getCombatTracker().getLastEntry();
                    if (currentCombatEntry != null && currentCombatEntry.getTime() == this.tickCount && Mth.equal(damage, currentCombatEntry.getDamage())) {
                        actualSource = currentCombatEntry.getSource();
                    }
                    if (this.currentDamageSource != null) {
                        this.showDamageDealt(damage, DamageType.get(actualSource, this.critical));
                    }
                    this.critical = false;
                }
            }
            this.entityData.set(DATA_HEALTH_ID, newHealth);
        }
    }

    @Unique
    @Override
    public boolean isDeadOrDying() {
        if (!isRemoved()) return false;
        return super.isDeadOrDying();
    }

    @Unique
    @Override
    public boolean isAlive() {
        return !isRemoved();
    }
}
