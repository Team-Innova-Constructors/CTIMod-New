package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import dev.xkmc.l2hostility.content.traits.legendary.PushPullTrait;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

@Mixin(value = PushPullTrait.class, remap = false)
public abstract class RepellingMixin {
    @Redirect(
            method = "tick(Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;m_5997_(DDD)V")
    )
    private void redirectMove(LivingEntity e, double x, double y, double z) {
        if (e instanceof Player player) {
            List<ItemStack> stacks = ToolUtils.Curios.getStacks(player);
            for (ItemStack curios : stacks) {
                if (ModifierUtil.getModifierLevel(curios, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId()) > 0) {
                    return;
                }
            }
            if (GetModifierLevel.EquipHasModifierlevel(player, CtiModifiers.STABLE_STEP.getId())) {
                return;
            }
        }
        e.setDeltaMovement(x, y, z);
    }
}
