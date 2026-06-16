package com.hoshino.cti.Event;

import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
                amount=amount * 1.25f;
            }
            if(lv.hasEffect(CtiEffects.nakshatra.get())){
                event.setAmount(amount * 2f);
            }
        }
    }
    @SubscribeEvent
    public static void LimingExtraDamage(LivingHurtEvent event){
        if(event.getSource().getEntity() instanceof Player player){
            if(EntityTickerManager.getInstance(player).hasTicker(CtiEntityTickers.DAWN_EXTRA_DAMAGE.get())){
                var ticker=EntityTickerManager.getInstance(player).getTicker(CtiEntityTickers.DAWN_EXTRA_DAMAGE.get());
                if (ticker != null) {
                    event.setAmount(event.getAmount() * 2 * ticker.level);
                }
            }
        }
    }
}
