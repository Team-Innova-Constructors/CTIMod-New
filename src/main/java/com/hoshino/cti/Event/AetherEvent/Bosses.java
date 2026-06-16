package com.hoshino.cti.Event.AetherEvent;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.hoshino.cti.Cti.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class Bosses {
    private static final DamageSource hurt_for_slider=new DamageSource("special_damage_for_slider").bypassArmor().bypassInvul().bypassMagic();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHurtStone(LivingDamageEvent event){
        if(event.getSource().getMsgId().equals("special_damage_for_slider"))return;
        if(event.getEntity() instanceof Slider slider){
            var source=event.getSource();
            var attacker=source.getEntity();
            if(attacker instanceof Player player){
                var stack=player.getMainHandItem();
                if(stack.getItem() instanceof ModifiableItem){
                    int digSpeed= ToolStack.from(stack).getStats().getInt(ToolStats.MINING_SPEED);
                    slider.hurt(hurt_for_slider,digSpeed * digSpeed / (digSpeed * 0.1f));
                }
            }
        }
    }
}
