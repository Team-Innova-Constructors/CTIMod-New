package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiModifiers;
import com.hoshino.cti.util.CurseUtil;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class CommonLivingHurt {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void LivingEntityEffectHurt(LivingHurtEvent event) {
        var entity = event.getEntity();
        if (event.getSource().getEntity() instanceof ServerPlayer player && player.hasEffect(CtiEffects.numerical_perception.get())) {
            event.setAmount(event.getAmount() * 2);
        }
        if (entity.getPersistentData().getBoolean("star_extra_hurt")) {
            event.setAmount(event.getAmount() * 33f);
        }
        if (event.getSource().getEntity() instanceof Player player && SuperpositionHandler.isTheCursedOne(player)) {
            var curseNBT = CurseUtil.getCurseCurioData(player);
            if (curseNBT == null) return;
            boolean activated = curseNBT.getBoolean("dongzhuo");
            if (activated) {
                event.setAmount(event.getAmount() * 1.35f);
            }
        }
        if (entity instanceof Player player && SuperpositionHandler.isTheCursedOne(player)) {
            var curseNBT = CurseUtil.getCurseCurioData(player);
            if (curseNBT == null) return;
            boolean activated = curseNBT.getBoolean("dongzhuo");
            if (activated) {
                event.setAmount(event.getAmount() * 1.35f);
            }
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

    @SubscribeEvent
    public static void onLivingJumpFDF(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (GetModifierLevel.getOffhandModifierlevel(player, CtiModifiers.FUCKING_DELTA_FORCE_STATIC_MODIFIER.getId()) > 0) {
                var amount =player.getPersistentData().getInt("tiao_yi_tiao");
                if(amount<3){
                    player.getPersistentData().putInt("tiao_yi_tiao",amount+1);
                }
                else {
                    var mobList=player.getLevel().getEntitiesOfClass(Mob.class,new AABB(player.getOnPos()).inflate(6));
                    for(Mob mob:mobList){
                        boolean shouldTeam=player.getRandom().nextInt(10)>4;
                        if(mob.getPersistentData().contains("player_teamed"))continue;
                        if(shouldTeam){
                            if(mob.getTarget()==player){
                                mob.setTarget(null);
                                mob.setDeltaMovement(new Vec3(0,0.5,0));
                                mob.getPersistentData().putBoolean("player_teamed",true);
                                double d0 = mob.getRandom().nextGaussian() * 0.02;
                                double d1 = mob.getRandom().nextGaussian() * 0.02;
                                double d2 = mob.getRandom().nextGaussian() * 0.02;
                                mob.level.addParticle(ParticleTypes.HEART, mob.getRandomX(1.0F), mob.getRandomY() + (double)0.5F, mob.getRandomZ(1.0F), d0, d1, d2);
                            }
                        }
                        else {
                            mob.getPersistentData().putBoolean("player_teamed",false);
                        }
                    }
                    player.getPersistentData().remove("tiao_yi_tiao");
                }
            }
        }
    }
}
