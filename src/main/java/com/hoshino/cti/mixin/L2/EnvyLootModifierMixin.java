package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.Items.RingOfNibelungen;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.DifficultyLevel;
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
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(value = EnvyLootModifier.class, remap = false)
public abstract class EnvyLootModifierMixin {
    @Redirect(method = "doApply", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/ForgeConfigSpec$DoubleValue;get()Ljava/lang/Object;"))
    private Object setEnvyLootRate(ForgeConfigSpec.DoubleValue instance, ObjectArrayList<ItemStack> list, LootContext context) {
        double base = 0.02;
        if (context.hasParam(LootContextParams.KILLER_ENTITY)) {
            var killer = context.getParam(LootContextParams.KILLER_ENTITY);
            if (killer instanceof Player player) {
                if (GetModifierLevel.EquipHasModifierlevel(player, new ModifierId("solidarytinker:littlecat"))) {
                    base +=0.05;
                }
                if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
                    base += 0.1;
                }
                if (CurioCompat.hasItem(player, CtiItem.ring_of_nibelungen.get())) {
                    int level = DifficultyLevel.ofAny(player);
                    var extra= RingOfNibelungen.getExtraRate(level);
                    return Math.min(1,extra+base);
                }
            }
        }
        return base;
    }
}
