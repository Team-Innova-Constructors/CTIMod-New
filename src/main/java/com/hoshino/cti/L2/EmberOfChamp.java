package com.hoshino.cti.L2;

import com.hoshino.cti.register.CtiHostilityTrait;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EmberOfChamp extends MobTrait {
    public EmberOfChamp() {
        super(ChatFormatting.GOLD);
    }

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event){
        var target = event.getNewTarget();
        if (target!=null){
            target.getCapability(MobTraitCap.CAPABILITY).ifPresent(cap->{
                if (cap.hasTrait(CtiHostilityTrait.EMBER_OF_CHAMP.get())){
                    if (target instanceof Mob mob&&mob.getTarget()!=event.getEntity())
                        event.setNewTarget(mob.getTarget());
                    else event.setCanceled(true);
                }
            });
            event.getEntity().getCapability(MobTraitCap.CAPABILITY).ifPresent(cap->{
                if (cap.hasTrait(CtiHostilityTrait.EMBER_OF_CHAMP.get())&&!(event.getNewTarget() instanceof Player)){
                    event.setCanceled(true);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event){
        var source = event.getSource();
        if (source.getEntity()==null) return;
        event.getEntity().getCapability(MobTraitCap.CAPABILITY).ifPresent(cap->{
            if (cap.hasTrait(CtiHostilityTrait.EMBER_OF_CHAMP.get())&&source.getEntity() instanceof LivingEntity living&&!(living instanceof Player))
                event.setCanceled(true);
        });
    }
}
