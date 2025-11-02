package com.hoshino.cti.Event;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.hoshino.cti.Cti;
import com.hoshino.cti.Entity.DisposibleFakePlayer;
import com.hoshino.cti.Modifier.Replace.FixedPurify;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiEffects;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.CurseUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.tools.TinkerTools;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class LivingEvents {
    @SubscribeEvent
    public static void onGobberKillEnderDragon(LivingDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon enderDragon && event.getSource().getEntity() instanceof Player player) {
            ItemStack skull = new ItemStack(Items.DRAGON_HEAD);
            if (player.getMainHandItem().getItem() == TinkerTools.cleaver.get()) {
                skull.setCount(9);
                enderDragon.spawnAtLocation(skull);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer player && SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE)) {
            Collection<MobEffectInstance> harmeffect = player.getActiveEffects();
            for (int i = 0; i < harmeffect.size(); i++) {
                MobEffectInstance effect = harmeffect.stream().toList().get(i);
                MobEffect harm = effect.getEffect();
                if (harm.getCategory() == MobEffectCategory.HARMFUL) {
                    player.removeEffect(harm);
                    player.kill();
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEffectApply(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            if (event.getEntity() instanceof Player player && SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE))
                event.setResult(Event.Result.DENY);
        } else if (event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.BENEFICIAL && EntityTickerManager.getInstance(event.getEntity()).hasTicker(CtiEntityTickers.ORACLE.get())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEffectApplyHigh(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.HARMFUL) {
            if (event.getEntity() instanceof Player player && SuperpositionHandler.hasCurio(player, EnigmaticItems.THE_CUBE))
                event.setResult(Event.Result.DENY);
            else if (EntityTickerManager.getInstance(event.getEntity()).hasTicker(CtiEntityTickers.ORACLE.get())) {
                var activeEffects = event.getEntity().activeEffects;
                var effect = event.getEffectInstance().getEffect();
                MobEffectInstance instance = activeEffects.get(effect);
                if (instance != null) {
                    instance.update(event.getEffectInstance());
                    event.getEntity().onEffectUpdated(instance, true, null);
                } else activeEffects.put(effect, event.getEffectInstance());
                event.getEntity().onEffectAdded(activeEffects.get(effect), null);
            }
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->{
                if (cap.get(FixedPurify.KEY_PURIFY,0)>0) event.setResult(Event.Result.DENY);
            });
        } else if (event.getEffectInstance().getEffect().getCategory() == MobEffectCategory.BENEFICIAL && EntityTickerManager.getInstance(event.getEntity()).hasTicker(CtiEntityTickers.ORACLE.get())) {
            event.setResult(Event.Result.DENY);
        }
    }


    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player1 && event.getEntity() instanceof Player player2) {
            if (!(player1 == player2)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onFakePlayerHurt(LivingHurtEvent event) {
        if (event.getEntity().getMaxHealth() > 10000 && event.getSource().getEntity() instanceof FakePlayer fakePlayer && !(fakePlayer instanceof DisposibleFakePlayer) && event.getAmount() < 2147483647) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPierceHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            MobEffectInstance instance = player.getEffect(CtiEffects.stress.get());
            if (instance != null && instance.getDuration() > 0) {
                int level = instance.getAmplifier() + 1;
                float red = Math.max(0.9f, 1 - 0.04f * level);
                event.setAmount(event.getAmount() * red);
            }
        }
        if (event.getEntity() instanceof Warden warden && event.getSource().getMsgId().equals("sonic_boom")) {
            event.setAmount(warden.getMaxHealth() / 4);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof EntityDragonBase && event.getSource().getEntity() != null && !(event.getSource().getEntity() instanceof Player))
            event.setCanceled(true);
        if (EntityTickerManager.getInstance(event.getEntity()).hasTicker(CtiEntityTickers.INVULNERABLE.get())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void glassWeakDamage(LivingDamageEvent event) {
        var source = event.getSource();
        var entity = event.getEntity();
        if (source.isBypassArmor() || source.isBypassMagic() || source.isBypassEnchantments() || source.isBypassInvul() || source.getEntity() == null)
            return;
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        var pos1 = new BlockPos(x + 3, y - 1, z + 3);
        var pos2 = new BlockPos(x - 3, y - 1, z - 3);
        var stateStream = entity.level.getBlockStates(new AABB(pos1, pos2));
        var list = stateStream.toList();
        int count = 0;
        for (BlockState state : list) {
            if (state.getBlock() != CtiBlock.aluminium_glass.get()) continue;
            count++;
        }
        event.setAmount(event.getAmount() * 1 - (Math.min(40f, count) / 100f));
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCurseDeath(LivingDeathEvent event){
        if(!(event.getEntity() instanceof Player player))return;
        if(!(SuperpositionHandler.isTheCursedOne(player)))return;
        if(event.isCanceled())return;
        int fre=CurseUtil.getDeathFrequency(player);
        CurseUtil.setDeathFrequency(player,fre+1);
        CurseUtil.setResoluteTime(player,0);
        if(fre<3)return;
        CurseUtil.setPunishTime(player,1);
        CurseUtil.setDeathFrequency(player,fre+1);
    }

    @SubscribeEvent
    public static void deathTimePunish(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!SuperpositionHandler.isTheCursedOne(player)) return;
        int fre=CurseUtil.getDeathFrequency(player);
        int time=CurseUtil.getPunishTime(player);
        if(time==0)return;
        event.setAmount((Math.max((fre-3) * 0.5f,0)+1) * event.getAmount());
    }
    @SubscribeEvent
    public static void punishWeakenPlayer(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        if (!SuperpositionHandler.isTheCursedOne(player)) return;
        int fre=CurseUtil.getDeathFrequency(player);
        int time=CurseUtil.getPunishTime(player);
        if(time==0)return;
        event.setAmount(event.getAmount() / Math.max( fre-3,0));
    }
    @SubscribeEvent
    public static void resoluteModify(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!SuperpositionHandler.isTheCursedOne(player)) return;
        int time=CurseUtil.getResoluteTime(player);
        if(time==0)return;
        event.setAmount(event.getAmount() * (1-(0.06f * time)));
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event){
        LivingEntity living = event.getEntity();
        var tickerManager = EntityTickerManager.getInstance(living);
        var tickerInstance = tickerManager.getTicker(CtiEntityTickers.SACRIFICE_SEAL.get());
        if (tickerInstance!=null){
            float maxHealth = living.getMaxHealth()*(1-0.1f*tickerInstance.level);
            if (living.getHealth()+event.getAmount()>maxHealth){
                living.setHealth(maxHealth);
                event.setCanceled(true);
            }
        }
    }
}
