package com.hoshino.cti.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHEffects;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PurifyTrait extends LegendaryTrait {
    public PurifyTrait(ChatFormatting color) {
        super((color));
        MinecraftForge.EVENT_BUS.addListener(this::MobEffectEvent);
    }

    private void MobEffectEvent(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        LazyOptional<MobTraitCap> optional = mob.getCapability(MobTraitCap.CAPABILITY);
        if (optional.resolve().isEmpty()) return;
        MobTraitCap cap = optional.resolve().get();
        MobTrait trait = CtiHostilityTrait.PURIFYTRAIT.get();
        if (!cap.hasTrait(trait) || event.getEffectInstance().getEffect().isBeneficial()) {
            return;
        }
        var target = mob.getTarget();
        if (target instanceof Player player) {
            if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId()) || GetModifierLevel.getTotalArmorModifierlevel(player, CtiModifiers.ARMOR_ORACLE.getId()) > 0) {
                return;
            }
            LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
            if (handler.resolve().isPresent()) {
                for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                    for (int k = 0; k < curios.getSlots(); ++k) {
                        ItemStack stack = curios.getStacks().getStackInSlot(k);
                        if (stack.is(LHItems.RING_REFLECTION.get()) || stack.is(LHItems.ABRAHADABRA.get())) {
                            return;
                        }
                    }
                }
            }
        }
        event.setResult(Event.Result.DENY);
    }

    @Override
    public void tick(@NotNull LivingEntity living, int level) {
        if (!(living instanceof Mob mob)) return;
        if (mob.tickCount % 3 != 0) return;
        var target = mob.getTarget();
        var list = mob.getActiveEffects();
        if (target instanceof Player player) {
            if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
                return;
            }
            if(GetModifierLevel.getTotalArmorModifierlevel(player,CtiModifiers.SHADOW_OF_VIGRID.getId())<3){
                if(player.getMaxHealth() * 20<mob.getMaxHealth()){
                    if(!player.isCreative()&&!player.isSpectator()){
                        if (player.getAbilities().mayfly || player.getAbilities().flying) {
                            player.getAbilities().flying = false;
                            player.getAbilities().mayfly = false;
                            player.onUpdateAbilities();
                        }
                    }
                }
            }
            LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
            if (handler.resolve().isPresent()) {
                for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                    for (int k = 0; k < curios.getSlots(); ++k) {
                        ItemStack stack = curios.getStacks().getStackInSlot(k);
                        if (stack.is(LHItems.ABRAHADABRA.get())) {
                            return;
                        }
                    }
                }
            }
        }
        removeAndHeal(mob, list, level);
    }

    public static void removeAndHeal(Mob mob, Collection<MobEffectInstance> mobEffectInstanceList, int level) {
        float healAmount = 0;
        List<MobEffectInstance> copyList = new ArrayList<>(mobEffectInstanceList);
        for (MobEffectInstance instance : copyList) {
            var effect=instance.getEffect();
            if (instance.getEffect().isBeneficial()) continue;
            if(effect==LHEffects.GRAVITY.get())continue;
            if(effect==LHEffects.MOONWALK.get())continue;
            if (instance.getDuration() > level * 7 * 20f) continue;
            mob.removeEffect(instance.getEffect());
            healAmount += instance.getDuration() / 20f;
        }
        if (healAmount > 0) {
            mob.heal(healAmount);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(getDescriptionId() + ".desc",
                mapLevel(i -> Component.literal(i + "")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDuration.get() * 100) + "%")
                        .withStyle(ChatFormatting.AQUA)),
                mapLevel(i -> Component.literal(Math.round(i * LHConfig.COMMON.drainDurationMax.get() / 20f) + "")
                        .withStyle(ChatFormatting.AQUA))
        ).withStyle(ChatFormatting.GRAY));
    }
}
