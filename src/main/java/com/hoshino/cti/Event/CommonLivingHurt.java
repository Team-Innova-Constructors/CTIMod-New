package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.CurseTimeUpdatePacket;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CurseUtil;
import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;

import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonLivingHurt {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void LivingEntityEffectHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player && player.hasEffect(CtiEffects.numerical_perception.get())) {
            event.setAmount(event.getAmount() * 2);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void PreventExplosionEvent(ExplosionEvent.Start event) {
        if (event.getExplosion().getSourceMob() instanceof Creeper creeper) {
            List<Player> playerlist = creeper.level.getEntitiesOfClass(Player.class, new AABB(creeper.getX() + 10, creeper.getY() + 10, creeper.getZ() + 10, creeper.getX() - 10, creeper.getY() - 10, creeper.getZ() - 10));
            for (Player player : playerlist) {
                if (GetModifierLevel.EquipHasModifierlevel(player, CtiModifiers.ExplosionPrevent.getId())) {
                    if (creeper.level instanceof ServerLevel level) {
                        level.playSound(null, player.getOnPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1, 1);
                        level.sendParticles(ParticleTypes.EXPLOSION, creeper.getX(), creeper.getY() + 0.5 * creeper.getBbHeight(), creeper.getZ(), 1, 0, 0, 0, 0);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }
@SubscribeEvent
public static void playerHurt(LivingDamageEvent event) {
    var source = event.getSource();
    if (event.getEntity() instanceof ServerPlayer player) {
        long time = CurseUtil.curseTime(player);
        if (!SuperpositionHandler.isTheCursedOne(player)) return;
        if (time < 288000) {
            if (source.getMsgId().equals("drown") || source.getMsgId().equals("inWall")) {
                event.setCanceled(true);
                return;
            }
        }
        if (time < 192000) {
            event.setAmount(event.getAmount() * 0.5f);
        }
    }
}
}
