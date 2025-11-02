package com.hoshino.cti.Modifier.Replace;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class Reflect extends ArmorModifier {
    @Override
    public void PlayerLivingHurt(LivingHurtEvent event, LivingEntity enemy, Player player) {
        if (ModifierLevel.EquipHasModifierlevel(player, this.getId()) && event.getSource() instanceof EntityDamageSource source && !source.isThorns()) {
            int level = ModifierLevel.getTotalArmorModifierlevel(player, this.getId()) + ModifierLevel.getEachHandsTotalModifierlevel(player, this.getId());
            enemy.hurt(new EntityDamageSource("goddamncrash", player).setThorns().setMagic(), event.getAmount() * 0.25f * level);
        }
    }
}
