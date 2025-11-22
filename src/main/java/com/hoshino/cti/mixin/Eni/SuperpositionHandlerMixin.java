package com.hoshino.cti.mixin.Eni;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuperpositionHandler.class)
public class SuperpositionHandlerMixin {
    @Inject(method = "constructLootPool",at = @At("HEAD"), cancellable = true,remap = false)
    private static void onLootTableLoaded(String poolName, float minRolls, float maxRolls, LootPoolEntryContainer.Builder<?>[] entries, CallbackInfoReturnable<LootPool> cir){
        LootPool.Builder poolBuilder = LootPool.lootPool();
        if(poolName.equals("spell")){
            cir.setReturnValue(poolBuilder.build());
        }
    }
}
