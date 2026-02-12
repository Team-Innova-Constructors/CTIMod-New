package com.hoshino.cti.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class HardSkin extends MobTrait {
    public HardSkin() {
        super(() -> 0x5149a2);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::LivingDamageEvent);
    }

    private void LivingDamageEvent(LivingDamageEvent event) {
        var source = event.getSource();
        var target = event.getEntity();
        if (source.isBypassInvul()||source.isBypassMagic()) return;
        if (target instanceof Mob mob) {
            LazyOptional<MobTraitCap> optional = mob.getCapability(MobTraitCap.CAPABILITY);
            if (optional.resolve().isPresent()) {
                MobTraitCap cap = optional.resolve().get();
                var level = cap.getTraitLevel(CtiHostilityTrait.HARD_SKIN.get());
                var blockAmount = target.getMaxHealth() * 0.05f + target.getArmorValue() * 0.5f * level;
                if (source.isBypassArmor()) {
                    blockAmount /= 4;
                }
                event.setAmount(Math.max(0,event.getAmount() - blockAmount));
            }
        }
    }
}
