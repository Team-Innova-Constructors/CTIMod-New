package com.hoshino.cti.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import com.hoshino.cti.register.CtiSounds;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.TraitEffectCache;
import dev.xkmc.l2hostility.content.traits.legendary.LegendaryTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public class FarewellGift extends LegendaryTrait {

    public FarewellGift() {
        super(ChatFormatting.GOLD);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::livingDeathEvent);
    }

    private void livingDeathEvent(LivingDeathEvent event) {
        var source = event.getSource();
        var attacker=source.getEntity();
        if(!(attacker instanceof Player player))return;
        var target = event.getEntity();
        if(event.isCanceled())return;
        if (target instanceof Mob mob) {
            LazyOptional<MobTraitCap> optional = mob.getCapability(MobTraitCap.CAPABILITY);
            if (optional.resolve().isPresent()) {
                MobTraitCap cap = optional.resolve().get();
                var level = cap.getTraitLevel(CtiHostilityTrait.FAREWELL_GIFT.get());
                if(level<1)return;
                cap.traitEvent((k, v) -> k.postHurtImpl(level, mob, player));
                var gift=new EntityDamageSource("farewell_gift",mob);
                var distance=player.distanceTo(target);
                if (distance>15)return;
                var scale=Math.max(1,15-distance)/15f;
                player.hurt(gift,mob.getMaxHealth() * 0.05f * level * scale);
                player.level.playSound(null,player.getOnPos(), CtiSounds.farewell_gift.get(), SoundSource.VOICE,1,1);
            }
        }
    }
}
