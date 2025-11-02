package com.hoshino.cti.Effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class Nakshatra extends StaticMobEffect{
    public Nakshatra() {
        super(MobEffectCategory.BENEFICIAL, 0xad0101);
        this.addAttributeModifier(Attributes.MAX_HEALTH, UUID.nameUUIDFromBytes(this.getDescriptionId().getBytes()).toString(),-0.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
