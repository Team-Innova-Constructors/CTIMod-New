package com.hoshino.cti.mixin.MowzieMixin;

import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.solidarytinkerModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityWroughtnaut.class)
public abstract class EntityWroughtnautMixin extends MowzieLLibraryEntity implements Enemy {
    @Shadow(remap = false) public boolean vulnerable;

    public EntityWroughtnautMixin(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }
    @Unique
    private int cti$passInvulTick;
    @Unique
    private int cti$corrodeTime;
    @Inject(method = "hurt",at = @At("HEAD"), cancellable = true)
    public void set(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(source.getMsgId().equals("lightningBolt")){
            this.setAnimation(NO_ANIMATION);
            cti$passInvulTick =80;
            this.setNoAi(true);
        }
        if(source.getEntity()instanceof Player player&& GetModifierLevel.getEachHandsTotalModifierlevel(player, solidarytinkerModifiers.CORRODE_STATIC_MODIFIER.getId())>0){
            cti$corrodeTime++;
        }
        if(cti$corrodeTime >20){
            this.setAnimation(NO_ANIMATION);
            cir.setReturnValue(super.hurt(source,amount));
        }
    }
    @Inject(method = "tick",at = @At("HEAD"))
    private void tick(CallbackInfo ci){
        if(cti$passInvulTick >1){
            cti$passInvulTick--;
            this.setNoAi(true);
            this.vulnerable=true;
        }
        else if(cti$passInvulTick==1){
            cti$passInvulTick--;
            this.vulnerable=false;
            setNoAi(false);
        }
    }
}
