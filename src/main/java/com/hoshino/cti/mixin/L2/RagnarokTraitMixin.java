package com.hoshino.cti.mixin.L2;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.c2h6s.etshtinker.init.EtshtinkerModifiers;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
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
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = RagnarokTrait.class, remap = false)
public abstract class RagnarokTraitMixin {
    @Inject(at = {@At("HEAD")}, method = {"allowSeal"}, cancellable = true)
    private static void Neutralization(EntitySlotAccess access, CallbackInfoReturnable<Boolean> cir) {
        //防止自身被封印
        List<Modifier> sealModifier = new ArrayList<>();
        sealModifier.add(solidarytinkerModifiers.TACTICSPROTECT_STATIC_MODIFIER.get());//星野
        sealModifier.add(solidarytinkerModifiers.TACTICSATTACK_STATIC_MODIFIER.get());//星野
        sealModifier.add(solidarytinkerModifiers.SUPERBLAZING_STATIC_MODIFIER.get());//白矮星
        sealModifier.add(solidarytinkerModifiers.COLLAPSE_STATIC_MODIFIER.get());//白矮星
        sealModifier.add(EtshtinkerModifiers.manaoverload_STATIC_MODIFIER.get());//魔灵皇
        sealModifier.add(EtshtinkerModifiers.perfectism.get());//魔灵皇
        sealModifier.add(EtshtinkerModifiers.trinitycurse_STATIC_MODIFIER.get());//三位一体
        sealModifier.add(CtiModifiers.timetojudge.get());//乌列尔
        sealModifier.add(CtiModifiers.celestiallight.get());//乌列尔
        sealModifier.add(CtiModifiers.disorder.get());//单机磁石
        sealModifier.add(CtiModifiers.ionize_induced.get());//感电水晶
        sealModifier.add(CtiModifiers.VALKYRIE_BLESS.get());//女武神
        sealModifier.add(CtiModifiers.PHOENIX.get());//凤凰
        sealModifier.add(TIModifiers.SEA_DREAM.get());//海梦
        for (Modifier modifier : sealModifier) {
            if (ModifierUtil.getModifierLevel(access.get(), modifier.getId()) > 0) {
                cir.setReturnValue(false);
            }
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
            if (SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE)) {
                ci.cancel();
            }
            if (SuperpositionHandler.hasCurio(player, EnigmaticItems.ENIGMATIC_ITEM)) {
                ci.cancel();
            }
            //这个列表里面的是只要身上4盔甲/主副有这个材料就会让诸神黄昏对所有装备都不生效
            List<Modifier> AllowModifier = new ArrayList<>();
            AllowModifier.add(CtiModifiers.INFINITY_STATIC_MODIFIER.get());//无尽
            AllowModifier.add(CtiModifiers.trauma.get());//恐怖
            AllowModifier.add(CtiModifiers.eventually.get());//恐怖
            AllowModifier.add(EtshtinkerModifiers.beconcerted_STATIC_MODIFIER.get());//星河马玉灵和奇迹物质
            AllowModifier.add(EtshtinkerModifiers.unknown_STATIC_MODIFIER.get());//宏原子
            AllowModifier.add(EtshtinkerModifiers.controllableannihl_STATIC_MODIFIER.get());//反中子武器
            AllowModifier.add(EtshtinkerModifiers.reactiveannihlarmor_STATIC_MODIFIER.get());//反中子护甲
            AllowModifier.add(CtiModifiers.FIERY_JAVELIN.get());//龙炎钢
            AllowModifier.add(CtiModifiers.PERMAFROST_REVIVAL.get());//龙霜钢
            AllowModifier.add(CtiModifiers.PLASMA_WAVE_SLASH.get());//龙霆钢
            AllowModifier.add(solidarytinkerModifiers.ANCIENTOCEAN_STATIC_MODIFIER.get());//墨冰武器
            AllowModifier.add(solidarytinkerModifiers.DEEPOCEANPROTECT_STATIC_MODIFIER.get());//墨冰护甲
            AllowModifier.add(CtiModifiers.STAR_DARGON_HIT_STATIC_MODIFIER.get());//星界龙神武器
            AllowModifier.add(CtiModifiers.starBlessStaticModifier.get());//星界龙神护甲
            for (Modifier modifier : AllowModifier) {
                if (ModifierLevel.EquipHasModifierlevel(target, modifier.getId())) {
                    ci.cancel();
                }
            }
            if (player.getItemBySlot(EquipmentSlot.HEAD).is(MekanismItems.MEKASUIT_HELMET.get()) || player.getItemBySlot(EquipmentSlot.CHEST).is(MekanismItems.MEKASUIT_BODYARMOR.get()) || player.getItemBySlot(EquipmentSlot.LEGS).is(MekanismItems.MEKASUIT_PANTS.get()) || player.getItemBySlot(EquipmentSlot.FEET).is(MekanismItems.MEKASUIT_BOOTS.get())) {
                ci.cancel();
            }
        }
    }
}

