package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiItem;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.init.loot.EnvyLootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnvyLootModifier.class, remap = false)
public abstract class EnvyLootModifierMixin {
    @Redirect(method = "doApply", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$DoubleValue;get()Ljava/lang/Object;"))
    private Object setEnvyLootRate(ForgeConfigSpec.DoubleValue instance, ObjectArrayList<ItemStack> list, LootContext context) {
        if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
            Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
            PlayerDifficulty pl = PlayerDifficulty.HOLDER.get(player);
            double total = 0;
            if (CurioCompat.hasItem(player, CtiItem.ring_of_nibelungen.get())) {
                total = pl.getLevelEditor().getTotal() * 0.0001;
            }
            return instance.get() + total;
        }
        return instance.get();
    }
}
