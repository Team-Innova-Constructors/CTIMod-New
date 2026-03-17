package com.hoshino.cti.mixin.GoalMixin;

import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {
    @Shadow @Nullable protected LivingEntity target;
    public NearestAttackableTargetGoalMixin(Mob pMob, boolean pMustSee) {
        super(pMob, pMustSee);
    }
    @Shadow @Final protected Class<?> targetType;


    @Shadow protected TargetingConditions targetConditions;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void cti$onCanUse(CallbackInfoReturnable<Boolean> cir) {
        boolean isTeamed = this.mob.getPersistentData().getBoolean("player_teamed");
        if (isTeamed) {
            if (Player.class.isAssignableFrom(this.targetType)) {
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(method = "findTarget", at = @At("HEAD"))
    private void cti$redirectTargetIfTeamed(CallbackInfo ci) {
        boolean isTeamed = this.mob.getPersistentData().getBoolean("player_teamed");
        if (isTeamed) {
            this.targetConditions.selector((entity) -> {
                if (entity instanceof Player) return false;
                if (entity.getPersistentData().getBoolean("player_teamed")) return false;
                return entity instanceof Monster;
            });
        }
    }
    @Redirect(
            method = "findTarget",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/goal/target/NearestAttackableTargetGoal;targetType:Ljava/lang/Class;", opcode = Opcodes.GETFIELD)
    )
    private Class<?> cti$redirectTargetType(NearestAttackableTargetGoal instance) {
        if (this.mob.getPersistentData().getBoolean("player_teamed")) {
            return Monster.class;
        }
        return this.targetType; // 返回原本的成员变量
    }

    @Inject(method = "canUse", at = @At(value = "RETURN",ordinal = 1), cancellable = true)
    private void setRange(CallbackInfoReturnable<Boolean> cir){
        if(this.target!=null&&this.target instanceof Player player){
            if(player.hasEffect(CtiEffects.covert.get())){
                var distance=target.position().distanceTo(mob.position());
                if(distance>3){
                    cir.setReturnValue(false);
                }
            }
            else if(ModifierLevel.EquipHasModifierlevel(player, CtiModifiers.starBlessStaticModifier.getId())){
                int mobLevel= DifficultyLevel.ofAny(mob);
                if(mobLevel<player.getArmorValue()+player.getMaxHealth()){
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
