package com.hoshino.cti.Items;

import com.hoshino.cti.Entity.Projectiles.FieryJavelinProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class FieryJavelinItem extends ArrowItem {
    public FieryJavelinItem() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return true;
    }

    public @NotNull AbstractArrow createArrow(Level pLevel, ItemStack pStack, LivingEntity pShooter) {
        return new FieryJavelinProjectile(pLevel, pShooter);
    }
}
