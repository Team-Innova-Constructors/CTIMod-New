package com.hoshino.cti.Effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static com.hoshino.cti.register.CtiEffects.curve_mapping;

public class CurveMapping extends StaticMobEffect {
    public CurveMapping() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtlevent);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void livinghurtlevent(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.getEffect(curve_mapping.get()) != null && player.hasEffect(curve_mapping.get())) {
            int a = player.getEffect(curve_mapping.get()).getAmplifier();
            if (a >= 0 && a <= 4) {
                event.setAmount(event.getAmount() * (0.75f - 0.05f * player.getEffect(curve_mapping.get()).getAmplifier()));
            } else if (a >= 4) {
                event.setAmount(event.getAmount() * (0.5f));
            }
        }
    }
}
