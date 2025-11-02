package com.hoshino.cti.mixin.L2;

import com.gjhi.tinkersinnovation.register.TinkersInnovationModifiers;
import com.hoshino.cti.content.environmentSystem.IEnvironmentalSource;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2hostility.content.traits.legendary.UndyingTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = UndyingTrait.class, remap = false)
public abstract class UndyingTraitMixin {
    @Inject(at = {@At("HEAD")}, method = {"onDeath"}, cancellable = true)
    private void onDeath(int level, LivingEntity entity, LivingDeathEvent event, CallbackInfo ci) {
        if (event.getSource().isBypassMagic()) {
            ci.cancel();
        } else if (event.getSource() instanceof IEnvironmentalSource) {
            ci.cancel();
        } else if (entity.getMaxHealth() <= 10 || entity.getPersistentData().contains("atomic_dec") || entity.getPersistentData().contains("quark_disassemble")) {
            ci.cancel();
        }else if (event.getSource().getEntity() instanceof Player player) {
            if (GetModifierLevel.getEachHandsTotalModifierlevel(player, TinkersInnovationModifiers.L2ComplementsModifier.curse_blade.getId()) > 0||GetModifierLevel.getEachHandsTotalModifierlevel(player, CtiModifiers.CURSED_ARROW.getId()) > 0) {
                ci.cancel();
            }
        }
        else if(entity instanceof Mob mob&&mob.getTarget() instanceof Player player){
            if (GetModifierLevel.getEachHandsTotalModifierlevel(player, TinkersInnovationModifiers.L2ComplementsModifier.curse_blade.getId()) > 0||GetModifierLevel.getEachHandsTotalModifierlevel(player, CtiModifiers.CURSED_ARROW.getId()) > 0) {
                ci.cancel();
            }
        }
    }
}
