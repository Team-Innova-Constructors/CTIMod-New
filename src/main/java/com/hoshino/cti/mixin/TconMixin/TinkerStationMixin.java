package com.hoshino.cti.mixin.TconMixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.tables.block.entity.table.TinkerStationBlockEntity;
import slimeknights.tconstruct.tools.data.ModifierIds;

@Mixin(TinkerStationBlockEntity.class)
public class TinkerStationMixin {
    @Inject(method = "onCraft",
            at = @At("HEAD"),
            remap = false)
    private void preventCrafting(Player player, ItemStack result, int amount, CallbackInfo ci) {
    }
}
