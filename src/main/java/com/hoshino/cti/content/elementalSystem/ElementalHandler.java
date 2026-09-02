package com.hoshino.cti.content.elementalSystem;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

@Mod.EventBusSubscriber
public class ElementalHandler{
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event){
        var target = event.getEntity();
        var ds = event.getSource();
        Random random = new Random();
        if (ds instanceof EntityDamageSource source){
            var attacker = source.getEntity();
            if (attacker instanceof Player player) {
                var playerInstance = EntityTickerManager.getInstance(player);
                if (triggerFiery(source)&&!playerInstance.hasTicker(CtiEntityTickers.FIERY_TOUCH_CD.get())) {
                    inflictBurnt(player,target,null,1);
                    playerInstance.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.FIERY_TOUCH_CD.get(), 1,5));
                }
            }
        }
    }
    public static List<String> FIERY_TRIGGER_IDS = List.of(
            DamageSource.ON_FIRE.msgId, DamageSource.IN_FIRE.msgId,
            DamageSource.LAVA.msgId, "cti.scorch"
    );
    public static boolean triggerFiery(EntityDamageSource source){
        if (source instanceof ElementalDamageSource ds)
            return ds.isFiery;
        else return FIERY_TRIGGER_IDS.contains(source.msgId);
    }
}
