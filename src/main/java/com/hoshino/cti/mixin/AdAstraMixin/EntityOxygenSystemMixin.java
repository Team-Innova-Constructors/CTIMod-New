package com.hoshino.cti.mixin.AdAstraMixin;

import com.hoshino.cti.library.modifier.OxygenS;
import com.hoshino.cti.util.ILivingEntityMixin;
import earth.terrarium.ad_astra.common.entity.system.EntityOxygenSystem;
import earth.terrarium.ad_astra.common.registry.ModDamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(value = EntityOxygenSystem.class,remap = false)
public class EntityOxygenSystemMixin {

    @Inject(method = "oxygenTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;m_6469_(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    private static void runHurt(LivingEntity entity, ServerLevel level, CallbackInfo ci){
        if(entity instanceof ServerPlayer serverPlayer){

            UUID uuid = serverPlayer.getUUID();

            int count = OxygenS.OXYGEN_TICK_MAP.getOrDefault(uuid, 0);
            count++;

            OxygenS.OXYGEN_TICK_MAP.put(uuid, count);
            float second=count/2f;

            if(count==10){
                serverPlayer.sendSystemMessage(Component.literal("快离开这里，没有氧气你受到的伤害会越来越高的"));
            }
            if(count==40){
                serverPlayer.sendSystemMessage(Component.literal("快离开这里，回去缓一口"));
            }
            if(count==80){
                serverPlayer.sendSystemMessage(Component.literal("这是最后警告，不然你会憋死的"));
            }
            if(count==120){
                serverPlayer.sendSystemMessage(Component.literal("祝你好运"));
            }

            DamageSource source=ModDamageSource.OXYGEN;
            var totalHurt=1.5f * second;
            if(second<20){
                entity.hurt(source,totalHurt);
            }
            else if(second<40){
                source.bypassMagic();
                entity.hurt(source,totalHurt);
            }
            else if(second<60){
                source.bypassMagic().bypassInvul().bypassEnchantments();
                entity.hurt(source,totalHurt);
            }
            else ((ILivingEntityMixin)entity).cti$strictHurt(source,totalHurt);

            ci.cancel();
        }

    }
    @Inject(method = "oxygenTick",at = @At("HEAD"), cancellable = true)
    private static void check(LivingEntity entity, ServerLevel level, CallbackInfo ci){
        if(entity instanceof ServerPlayer serverPlayer){
            if(!OxygenS.checkNeedOxygen(serverPlayer)){
                ci.cancel();
            }
        }
    }



    @Inject(method = "consumeOxygen",at = @At(value = "INVOKE", target = "Learth/terrarium/ad_astra/common/item/armor/SpaceSuit;consumeSpaceSuitOxygen(Lnet/minecraft/world/entity/LivingEntity;J)V"))
    private static void aw(LivingEntity entity, CallbackInfo ci){
        if(entity instanceof ServerPlayer serverPlayer){
            OxygenS.checkAndRecoverOxygen(serverPlayer);
        }
    }
}
