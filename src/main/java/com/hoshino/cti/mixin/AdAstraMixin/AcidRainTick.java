package com.hoshino.cti.mixin.AdAstraMixin;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CtiTagkey;
import earth.terrarium.ad_astra.common.entity.system.EntityAcidRainSystem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

@Mixin(value = EntityAcidRainSystem.class, remap = false)
public class AcidRainTick {
    @Inject(method = "acidRainTick", at = @At("HEAD"), cancellable = true)
    private static void acidRainPrevent(LivingEntity entity, ServerLevel level, CallbackInfo ci) {
        boolean b = false;
        for (EquipmentSlot slot : List.of(EquipmentSlot.CHEST, EquipmentSlot.FEET, EquipmentSlot.HEAD, EquipmentSlot.LEGS)) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.getItem() instanceof IModifiable iModifiable) {
                ToolStack tool = ToolStack.from(stack);
                if (tool.getModifierLevel(CtiModifiers.space_suit.get()) > 1) {
                    b = true;
                }
            } else if (stack.getTags().toList().contains(CtiTagkey.OXYGEN_REGEN)) {
                b = true;
            }
        }
        if (b) {
            ci.cancel();
        }

    }
}
