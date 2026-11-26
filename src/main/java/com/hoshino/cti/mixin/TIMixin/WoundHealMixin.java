package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.armor.WoundHeal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = WoundHeal.class,remap = false)
public class WoundHealMixin {
    @Inject(method = "onTakeAttack",at = @At(value = "HEAD"), cancellable = true)
    public void set(IToolStackView tool, int level, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage, CallbackInfo ci){
        var living =context.getEntity();
        if(!(living instanceof Player player)){
            ci.cancel();
            return;
        }
        player.heal(player.getMaxHealth() * 0.12f);
    }
}
