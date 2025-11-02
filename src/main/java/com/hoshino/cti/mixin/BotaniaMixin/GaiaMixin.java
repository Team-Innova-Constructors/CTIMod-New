package com.hoshino.cti.mixin.BotaniaMixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.List;
import java.util.UUID;

@Mixin(GaiaGuardianEntity.class)
public abstract class GaiaMixin extends Mob {
    @Shadow(remap = false)
    @Final
    private List<UUID> playersWhoAttacked;

    @Shadow(remap = false) private boolean hardMode;

    @Shadow(remap = false) private int mobSpawnTicks;

    protected GaiaMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Inject(method = "m_6667_", at = @At("HEAD"), remap = false)
    private void setKiller(DamageSource source, CallbackInfo ci) {
        var entity = source.getEntity();
        if (entity instanceof Player player) {
            if(!playersWhoAttacked.contains(player.getUUID())){
                this.playersWhoAttacked.add(player.getUUID());
            }
            this.lastHurtByPlayer=player;
        }
        if(this.mobSpawnTicks>0){
            super.dropFromLootTable(source,true);
        }
    }
    @Inject(method = "m_6469_", at = @At("HEAD"), remap = false)
    private void setPlayersWhoAttacked(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        var entity = source.getEntity();
        if (entity instanceof Player player) {
            if(!playersWhoAttacked.contains(player.getUUID())){
                this.playersWhoAttacked.add(player.getUUID());
            }
            this.lastHurtByPlayer=player;
        }
    }
    @Inject(method = "m_7582_",at = @At("HEAD"), cancellable = true,remap = false)
    private void setLoot(CallbackInfoReturnable<ResourceLocation> cir){
        cir.setReturnValue(ResourceLocationHelper.prefix(this.hardMode ? "gaia_guardian_2" : "gaia_guardian"));
    }
}
