package com.hoshino.cti.Effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static com.hoshino.cti.register.CtiEffects.solid;
import static com.hoshino.cti.register.CtiEffects.solid_armor;

public class SolidArmor extends StaticMobEffect {
    public SolidArmor() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::OnLivingHurt);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void OnLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.hasEffect(solid_armor.get())) {
            if (player.getEffect(solid_armor.get())!=null) {
                int a = player.getEffect(solid_armor.get()).getAmplifier() - 1;
                if (a>15)a=15;
                event.setAmount(event.getAmount() * (0.75f - 0.05f * a));
            }
        }
    }
}
