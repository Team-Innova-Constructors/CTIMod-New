package com.hoshino.cti.mixin.IAFMixin;

import com.github.alexthe666.iceandfire.entity.ai.DragonAITargetNonTamed;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DragonAITargetNonTamed.class)
public abstract class DragonAITargetNonTamedMixin<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public DragonAITargetNonTamedMixin(Mob pMob, Class<T> pTargetType, boolean pMustSee) {
        super(pMob, pTargetType, pMustSee);
    }

    @Inject(method = "canUse",at = @At("HEAD"),cancellable = true)
    private void ignoreNonPlayer(CallbackInfoReturnable<Boolean> cir){
        if (!(target instanceof Player)) cir.setReturnValue(false);
    }
}
