package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.traits.goals.EnderTrait;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EnderTrait.class,remap = false)
public class TeleportMixin {
    @Inject(method = "tick",at = @At(value = "HEAD"), cancellable = true)
    private void mobTick(LivingEntity mob, int level, CallbackInfo ci){
        if(!mob.level.isClientSide()&&mob instanceof Mob mob1){
            var entity=mob1.getTarget();
            if(entity instanceof ServerPlayer serverPlayer){
               if(GetModifierLevel.CurioHasModifierlevel(serverPlayer, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())||GetModifierLevel.EquipHasModifierlevel(serverPlayer, CtiModifiers.qcfStaticModifier.getId())){
                   ci.cancel();
               }
            }
        }
    }
    @Inject(method = "onAttackedByOthers",at = @At("HEAD"), cancellable = true)
    private void onAttack(int level, LivingEntity entity, LivingAttackEvent event, CallbackInfo ci){
        var entity1=event.getSource().getEntity();
        if(entity1 instanceof LivingEntity lv){
            if(GetModifierLevel.CurioHasModifierlevel(lv, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())||GetModifierLevel.EquipHasModifierlevel(lv, CtiModifiers.qcfStaticModifier.getId())){
                ci.cancel();
            }
        }
    }
}
