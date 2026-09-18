package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.entity.BulletType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BulletType.class)
public class BulletTypeMixin {
    @Inject(method = "onAttackedByOthers",at = @At("HEAD"),remap = false)
    private void set(int level, LivingEntity entity, LivingAttackEvent event, CallbackInfo ci){
        if (event.getSource().getEntity() instanceof ShulkerBullet) {
            event.setCanceled(true);
        } else {
            if (event.getSource().isExplosion()&&!(event.getSource().getEntity() instanceof Player)){
                event.setCanceled(true);
            }
        }
    }
}
