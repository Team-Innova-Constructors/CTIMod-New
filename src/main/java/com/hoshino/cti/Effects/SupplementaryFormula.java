package com.hoshino.cti.Effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static com.hoshino.cti.register.CtiEffects.supplementary_formula;

public class SupplementaryFormula extends StaticMobEffect {
    public SupplementaryFormula() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtlevent);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void livinghurtlevent(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.getEffect(supplementary_formula.get()) != null && player.hasEffect(supplementary_formula.get())) {
            int a = player.getEffect(supplementary_formula.get()).getAmplifier();
            if (a >= 0 && a <= 7) {
                event.setAmount(event.getAmount() * (0.75f - 0.05f * player.getEffect(supplementary_formula.get()).getAmplifier()));
            } else if (a >= 7) {
                event.setAmount(event.getAmount() * (0.35f));
            }
        }
    }
}
