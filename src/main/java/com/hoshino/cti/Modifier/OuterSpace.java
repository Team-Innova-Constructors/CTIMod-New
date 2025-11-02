package com.hoshino.cti.Modifier;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;

public class OuterSpace extends NoLevelsModifier {
    public OuterSpace() {
        MinecraftForge.EVENT_BUS.addListener(this::onLivingDamage);
    }

    private void onLivingDamage(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity living && living != event.getEntity()) {
            if (GetModifierLevel.getTotalArmorModifierlevel(living, this.getId()) > 0) {
                if (living.getLevel().dimension() != Level.OVERWORLD) {
                    event.setAmount(event.getAmount() * 1.25f);
                }
                if (!event.getSource().isDamageHelmet()) {
                    event.getEntity().invulnerableTime = 0;
                    event.getEntity().hurt(DamageSource.FALLING_BLOCK, event.getAmount() * 0.2f);
                    event.getEntity().invulnerableTime = 0;
                }
            }
        }
    }
}
