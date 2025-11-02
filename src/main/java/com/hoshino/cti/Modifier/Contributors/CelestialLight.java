package com.hoshino.cti.Modifier.Contributors;

import com.marth7th.solidarytinker.extend.superclass.ArmorModifier;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;

public class CelestialLight extends ArmorModifier {
    @Override
    public boolean havenolevel() {
        return true;
    }

    @Override
    public void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() != null && ModifierLevel.getMainhandModifierlevel(event.getEntity(), this.getId()) > 0 && ModifierLevel.EquipHasModifierlevel(event.getEntity(), this.getId())) {
            if (event.getEntity() instanceof Player player) {
                if (event.getAmount() > player.getHealth() && !player.getCooldowns().isOnCooldown(player.getItemBySlot(EquipmentSlot.MAINHAND).getItem())) {
                    event.setCanceled(true);
                    player.invulnerableTime = 120;
                    player.level.explode(player, player.getX(), player.getY(), player.getZ(), 3, false, Explosion.BlockInteraction.NONE);
                    player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.MAINHAND).getItem(), 600);
                    double x = player.getX();
                    double y = player.getY();
                    double z = player.getZ();
                    List<Mob> mobbbb = player.level.getEntitiesOfClass(Mob.class, new AABB(x + 10, y + 10, z + 10, x - 10, y - 10, z - 10));
                    for (Mob targets : mobbbb) {
                        if (targets != null) {
                            targets.setRemainingFireTicks(20000);
                        }
                    }
                    if (event.getSource().getEntity() instanceof LivingEntity enemy && event.getSource().getEntity().getTags().contains("wick")) {
                        enemy.setRemainingFireTicks(20000);
                    }
                    if (event.getSource() == DamageSource.explosion(player)) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
}
