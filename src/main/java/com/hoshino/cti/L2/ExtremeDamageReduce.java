package com.hoshino.cti.L2;

import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExtremeDamageReduce extends LegendaryTrait {
    public ExtremeDamageReduce(ChatFormatting format) {
        super(format);
    }

    @Override
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().isBypassArmor()) event.setAmount(event.getAmount()*0.2f);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(le.getType());
        return location!=null&& location.getNamespace().equals("cataclysm");
    }
}
