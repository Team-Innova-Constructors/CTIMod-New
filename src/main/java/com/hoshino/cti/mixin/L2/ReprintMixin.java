package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.register.CtiModifiers;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import com.xiaoyue.tinkers_ingenuity.utils.ToolUtils;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.highlevel.ReprintTrait;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.List;

@Mixin(value = ReprintTrait.class, remap = false)
public class ReprintMixin {
    @Inject(at = {@At("HEAD")}, method = {"onHurtTarget"}, cancellable = true)
    private void print(int level, LivingEntity attacker, AttackCache cache, TraitEffectCache traitCache, CallbackInfo ci) {
        if (ModifierLevel.EquipHasModifierlevel(cache.getAttackTarget(), CtiModifiers.encryptStaticModifier.getId())) {
            ci.cancel();
        }
        List<ItemStack> curio = ToolUtils.Curios.getStacks(cache.getAttackTarget());
        for (ItemStack curios : curio) {
            if (ModifierUtil.getModifierLevel(curios, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId()) > 0) {
                ci.cancel();
            }
        }
    }
}
