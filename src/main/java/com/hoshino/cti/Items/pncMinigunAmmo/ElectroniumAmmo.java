package com.hoshino.cti.Items.pncMinigunAmmo;

import com.c2h6s.etshtinker.init.etshtinkerEffects;
import me.desht.pneumaticcraft.common.config.ConfigHelper;
import me.desht.pneumaticcraft.common.core.ModUpgrades;
import me.desht.pneumaticcraft.common.item.minigun.AbstractGunAmmoItem;
import me.desht.pneumaticcraft.common.minigun.Minigun;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ElectroniumAmmo extends AbstractGunAmmoItem {
    @Override
    public int getAmmoColor(ItemStack itemStack) {
        return 0x00208099;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 2048;
    }

    @Override
    protected float getDamageMultiplier(Entity target, ItemStack ammoStack) {
        return 36f;
    }

    @Override
    public float getAirUsageMultiplier(Minigun minigun, ItemStack ammoStack) {
        return 0.005f;
    }

    public float getRangeMultiplier(ItemStack ammoStack) {
        return 2f;
    }

    protected DamageSource getDamageSource(Minigun minigun, Float amount) {
        return DamageSource.playerAttack(minigun.getPlayer()).bypassMagic().bypassArmor();
    }

    @Override
    public int onTargetHit(Minigun minigun, ItemStack ammo, Entity target) {
        int times = 1;
        int nSpeed = minigun.getUpgrades(ModUpgrades.SPEED.get());
        for (int i = 0; i < nSpeed; i++) {
            if (minigun.getWorld().random.nextInt(100) < 20) times++;
        }
        double dmgMult = getDamageMultiplier(target, ammo);
        if (dmgMult > 0) {
            if (target instanceof LivingEntity || target instanceof EnderDragonPart || target instanceof EndCrystal) {
                target.invulnerableTime = 0;
                target.hurt(getDamageSource(minigun, (float) (ConfigHelper.common().minigun.baseDamage.get() * dmgMult * times)), (float) (ConfigHelper.common().minigun.baseDamage.get() * dmgMult * times));
                if (target instanceof LivingEntity living) {
                    living.forceAddEffect(new MobEffectInstance(etshtinkerEffects.ionized.get(), 50, 9), minigun.getPlayer());
                    living.forceAddEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 4), minigun.getPlayer());
                }
            } else if (target instanceof ShulkerBullet || target instanceof AbstractHurtingProjectile) {
                target.discard();
            }
        }
        return times;
    }

    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            list.add(Component.translatable("cti.tooltip.item.electronium_ammo").withStyle(ChatFormatting.AQUA));
        } else {
            list.add(Component.translatable("cti.tooltip.item.shift").withStyle(ChatFormatting.AQUA));
        }
    }
}
