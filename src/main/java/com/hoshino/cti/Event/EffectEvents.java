package com.hoshino.cti.Event;

import com.hoshino.cti.register.CtiEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class EffectEvents {
    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event){
        if(event.getEntity().hasEffect(CtiEffects.strong.get())){
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event){
        float amount=event.getAmount();
        var attacker=event.getSource().getEntity();
        if(event.getEntity().hasEffect(CtiEffects.heng.get())){
            event.setAmount(amount * 0.75f);
        }
        if(attacker instanceof LivingEntity lv){
            if(lv.hasEffect(CtiEffects.ha.get())){
                event.setAmount(amount * 1.25f);
            }
            if(lv.hasEffect(CtiEffects.nakshatra.get())){
                event.setAmount(amount * 2f);
            }
        }
    }
}
