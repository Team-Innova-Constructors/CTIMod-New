package com.hoshino.cti.content.entityTicker.tickers;

import com.hoshino.cti.content.entityTicker.EntityTicker;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class VulnerableTicker extends EntityTicker {
    public VulnerableTicker() {
        super(MobEffectCategory.BENEFICIAL);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        var instance = EntityTickerManager.getInstance(event.getEntity());
        var tickerInstance = instance.getTicker(CtiEntityTickers.VULNERABLE.get());
        if (tickerInstance!=null){
            event.setAmount(event.getAmount()*(1+(tickerInstance.level*0.2f)));
        }
    }
}
