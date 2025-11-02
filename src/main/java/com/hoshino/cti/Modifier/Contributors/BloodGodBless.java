package com.hoshino.cti.Modifier.Contributors;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class BloodGodBless extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    public BloodGodBless() {
        MinecraftForge.EVENT_BUS.addListener(this::LivingDamageBoost);
    }

    private void LivingDamageBoost(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player && GetModifierLevel.EquipHasModifierlevel(player, this.getId())) {
            float bonus = (player.getMaxHealth() - player.getHealth()) * 0.008f;
            event.setAmount(event.getAmount() * (1 + bonus));
        }
    }
}
