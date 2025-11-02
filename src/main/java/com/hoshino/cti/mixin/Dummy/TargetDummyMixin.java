package com.hoshino.cti.mixin.Dummy;

import dev.xkmc.l2complements.init.registrate.LCItems;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.mehvahdjukaar.dummmmmmy.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(TargetDummyEntity.class)
public abstract class TargetDummyMixin extends Mob {
    protected TargetDummyMixin(EntityType<? extends Mob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;displayClientMessage(Lnet/minecraft/network/chat/Component;Z)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void summon(CallbackInfo ci, BlockPos onPos, BlockState onState, CombatTracker tracker, float combatDuration, CommonConfigs.DpsMode dpsMode, boolean dynamic, float seconds, float dps, List outOfCombat, Iterator var10, Map.Entry e, ServerPlayer p, int timer, boolean showMessage){
        if (this.lastHurtByPlayer != null && dps > 1000 && this.lastHurtByPlayer.distanceTo(this) < 3) {
            ItemEntity shard = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(LCItems.SPACE_SHARD.get()));
            this.level.addFreshEntity(shard);
        }
    }
}
