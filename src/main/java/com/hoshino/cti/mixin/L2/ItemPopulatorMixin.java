package com.hoshino.cti.mixin.L2;

import dev.xkmc.l2hostility.content.logic.ItemPopulator;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemPopulator.class)
public class ItemPopulatorMixin {
    @Inject(method = "fillEnch",at = @At("HEAD"), cancellable = true,remap = false)
    private static void a(int level, RandomSource source, ItemStack stack, EquipmentSlot slot, CallbackInfo ci){
        ci.cancel();
    }
    @Redirect(method = "postFill", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;m_220292_(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/item/ItemStack;IZ)Lnet/minecraft/world/item/ItemStack;"),remap = false)
    private static ItemStack cti$cancelRandomEnchant(RandomSource random, ItemStack stack, int level, boolean allowTreasure) {
        return stack;
    }
}
