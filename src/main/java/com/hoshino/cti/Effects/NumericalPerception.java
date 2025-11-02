package com.hoshino.cti.Effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class NumericalPerception extends StaticMobEffect {
    public NumericalPerception() {
        super(MobEffectCategory.BENEFICIAL, 16769263);
        MinecraftForge.EVENT_BUS.addListener(this::livinghurtlevent);
        super.addAttributeModifier(Attributes.ATTACK_DAMAGE, "B764A8F6-88CE-85C1-C5F9-C832E5335E2D", 0.1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        super.addAttributeModifier(ForgeMod.ATTACK_RANGE.get(), "0F40ADF0-3D9A-D8B7-79EE-27AB356A0050", 0.5, AttributeModifier.Operation.ADDITION);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    private void livinghurtlevent(LivingHurtEvent event) {

    }
}
