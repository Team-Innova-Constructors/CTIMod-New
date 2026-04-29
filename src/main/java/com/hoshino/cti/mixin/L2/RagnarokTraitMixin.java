package com.hoshino.cti.mixin.L2;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.c2h6s.etshtinker.init.EtshtinkerModifiers;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import com.marth7th.solidarytinker.register.solidarytinkerModifierMekEtsh;
import com.marth7th.solidarytinker.register.solidarytinkerModifiers;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import com.xiaoyue.tinkers_ingenuity.register.TIModifiers;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.traits.legendary.RagnarokTrait;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RagnarokTrait.class, remap = false)
public abstract class RagnarokTraitMixin {
    @Inject(at = {@At("HEAD")}, method = {"allowSeal"}, cancellable = true)
    private static void Neutralization(EntitySlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        //防止自身被封印
        List<Modifier> sealModifier = new ArrayList<>();
        sealModifier.add(EtshtinkerModifiers.manaoverload_STATIC_MODIFIER.get());//魔灵皇
        sealModifier.add(EtshtinkerModifiers.perfectism.get());//魔灵皇
        sealModifier.add(EtshtinkerModifiers.trinitycurse_STATIC_MODIFIER.get());//三位一体

        sealModifier.add(TIModifiers.SEA_DREAM.get());//海梦
        for (Modifier modifier : sealModifier) {
            if (ModifierUtil.getModifierLevel(access.get(), modifier.getId()) > 0) {
                cir.setReturnValue(false);
            }
        }
        if (ModifierUtil.getModifierLevel(access.get(),new ModifierId("cti:the_relic")) > 0) {
            cir.setReturnValue(false);
        }
        if (access.get().is(EnigmaticItems.CURSED_RING)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"postHurtImpl"}, cancellable = true)
    private void ignore(int level, LivingEntity attacker, LivingEntity target, CallbackInfo ci) {
        if (target instanceof Player player) {
            //戒指专属
            if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
                ci.cancel();
            }
            if (GetModifierLevel.CurioHasModifierlevel(player, new ModifierId("solidarytinker:deepoceanchew"))) {
                ci.cancel();
            }
            if (SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE)) {
                ci.cancel();
            }
            if (SuperpositionHandler.hasCurio(player, EnigmaticItems.ENIGMATIC_ITEM)) {
                ci.cancel();
            }
            //这个列表里面的是只要身上4盔甲/主副有这个材料就会让诸神黄昏对所有装备都不生效
            List<Modifier> allowModifier = new ArrayList<>();
            allowModifier.add(EtshtinkerModifiers.beconcerted_STATIC_MODIFIER.get());//奇迹物质
            allowModifier.add(EtshtinkerModifiers.unknown_STATIC_MODIFIER.get());//宏原子


            for (Modifier modifier : allowModifier) {
                if (ModifierLevel.EquipHasModifierlevel(target, modifier.getId())) {
                    ci.cancel();
                }
            }
            if (ModifierLevel.EquipHasModifierlevel(target, new ModifierId("cti:shadow_of_vigrid"))) {
                ci.cancel();
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(MekanismItems.MEKASUIT_HELMET.get()) || player.getItemBySlot(EquipmentSlot.CHEST).is(MekanismItems.MEKASUIT_BODYARMOR.get()) || player.getItemBySlot(EquipmentSlot.LEGS).is(MekanismItems.MEKASUIT_PANTS.get()) || player.getItemBySlot(EquipmentSlot.FEET).is(MekanismItems.MEKASUIT_BOOTS.get())) {
                ci.cancel();
            }
        }
    }
}

