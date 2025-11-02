package com.hoshino.cti.register;

import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import com.hoshino.cti.Entity.Projectiles.TinkerRailgunProjectile;
import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;


public class CtiRailgunProjectile {
    public static void register() {
        RailgunHandler.registerProjectile(() -> Ingredient.of(new ItemStack(TinkerToolParts.toolHandle)), new RailgunHandler.IRailgunProjectile() {
            @Override
            public Entity getProjectile(@Nullable Player shooter, ItemStack ammo, Entity defaultProjectile) {
                if (shooter != null) {
                    TinkerRailgunProjectile projectile = new TinkerRailgunProjectile(CtiEntity.tinker_railgun.get(), shooter.level, ammo, TinkerTools.sword.get());
                    projectile.setOwner(shooter);
                    projectile.pickup = AbstractArrow.Pickup.ALLOWED;
                    projectile.damageMul = 50;
                    projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 20, 0);
                    projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
                    return projectile;
                }
                return defaultProjectile;
            }
        });
        RailgunHandler.registerProjectile(() -> Ingredient.of(new ItemStack(TinkerToolParts.toughHandle)), new RailgunHandler.IRailgunProjectile() {
            @Override
            public Entity getProjectile(@Nullable Player shooter, ItemStack ammo, Entity defaultProjectile) {
                if (shooter != null) {
                    TinkerRailgunProjectile projectile = new TinkerRailgunProjectile(CtiEntity.tinker_railgun.get(), shooter.level, ammo, TIItems.RAPIER.get());
                    projectile.setOwner(shooter);
                    projectile.pickup = AbstractArrow.Pickup.ALLOWED;
                    projectile.setPierceLevel((byte) 2);
                    projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0.0F, 8, 0);
                    projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
                    return projectile;
                }
                return defaultProjectile;
            }
        });
    }
}
