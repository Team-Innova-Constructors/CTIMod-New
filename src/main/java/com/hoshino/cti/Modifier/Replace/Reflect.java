package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Reflect extends ArmorModifier {
    @Override
    public void PlayerLivingHurt(LivingHurtEvent event, LivingEntity enemy, Player player) {
        if (GetModifierLevel.equipHasModifierLevel(player, this.getId()) && event.getSource() instanceof EntityDamageSource source && !source.isThorns()) {
            int level = GetModifierLevel.getTotalArmorModifierlevel(player, this.getId()) + GetModifierLevel.getEachHandsTotalModifierLevel(player, this.getId());
            enemy.invulnerableTime=0;
            enemy.hurt(new EntityDamageSource("goddamncrash", player).setThorns().setMagic(), event.getAmount() * 0.25f * level);
        }
    }
}
