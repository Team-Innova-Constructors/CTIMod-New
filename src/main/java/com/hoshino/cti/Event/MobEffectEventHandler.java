package com.hoshino.cti.Event;

import com.hoshino.cti.register.CtiEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

public class MobEffectEventHandler {
    public MobEffectEventHandler() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::HasResoluteEffect);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::ResoluteEffectProtect);
    }

    private void HasResoluteEffect(net.minecraftforge.event.entity.living.MobEffectEvent.Remove event) {
        if (event.getEntity() != null && event.getEntity().hasEffect(CtiEffects.resolute.get())) {
            if (event.getEffectInstance() != null) {
                if (event.getEffectInstance().getEffect() == CtiEffects.resolute.get() || event.getEffectInstance().getEffect().isBeneficial()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    private void ResoluteEffectProtect(net.minecraftforge.event.entity.living.MobEffectEvent.Applicable event) {
        if (event.getEntity() != null) {
            if (event.getEntity() != null && event.getEntity().hasEffect(CtiEffects.resolute.get()) && !event.getEffectInstance().getEffect().isBeneficial()) {
                event.setResult(Event.Result.DENY);
            }
        }
    }

}
