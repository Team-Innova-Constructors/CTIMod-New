package com.hoshino.cti.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import com.hoshino.cti.util.EffectUtil;
import com.hoshino.cti.util.method.GetModifierLevel;
import com.marth7th.solidarytinker.register.TinkerCuriosModifier;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.init.events.attack.AttackCache;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.TConstruct;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;

public class PurifyTrait extends MobTrait {
    public PurifyTrait(IntSupplier color) {
        super((color));
        MinecraftForge.EVENT_BUS.addListener(this::MobEffectEvent);
    }

    @Override
    public void postInit(@NotNull LivingEntity mob, int lv) {
        var cap = MobTraitCap.HOLDER.get(mob);
        var manager = LHTraits.TRAITS.get().tags();
        if (manager == null) return;
        for (int i = 0; i < 4; i++) {
            var opt = manager.getTag(LHTraits.POTION).getRandomElement(mob.getRandom());
            if (opt.isEmpty()) continue;
            var trait = opt.get();
            if (trait.allow(mob) && !cap.hasTrait(trait)) {
                cap.setTrait(trait, lv);
                return;
            }
        }
    }

    private void MobEffectEvent(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Mob mob) {
            LazyOptional<MobTraitCap> optional = mob.getCapability(MobTraitCap.CAPABILITY);
            if (optional.resolve().isPresent()) {
                MobTraitCap cap = optional.resolve().get();
                Set<MobTrait> set = cap.traits.keySet();
                for (int i = 0; i < set.stream().toList().size(); i++) {
                    MobTrait trait = CtiHostilityTrait.PURIFYTRAIT.get();
                    List<Player> playerlist = mob.level.getEntitiesOfClass(Player.class, new AABB(mob.getX() + 10, mob.getY() + 10, mob.getZ() + 10, mob.getX() - 10, mob.getY() - 10, mob.getZ() - 10));
                    for (Player player : playerlist) {
                        if (GetModifierLevel.CurioHasModifierlevel(player, TinkerCuriosModifier.BHA_STATIC_MODIFIER.getId())) {
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
                        if (cap.hasTrait(trait) && !event.getEffectInstance().getEffect().isBeneficial()) {
                            event.setResult(Event.Result.DENY);
                            return;
                        }
                    }
                }
            }
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
