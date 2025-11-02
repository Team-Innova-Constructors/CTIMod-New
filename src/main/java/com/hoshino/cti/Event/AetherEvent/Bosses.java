package com.hoshino.cti.Event.AetherEvent;

import com.aetherteam.aether.entity.AetherEntityTypes;
import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import com.marth7th.solidarytinker.register.solidarytinkerModifiers;
import com.marth7th.solidarytinker.util.method.ModifierLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void SunSpiritCool(LivingAttackEvent event) {
        if (event.getSource().getEntity() instanceof Player player && ModifierLevel.EquipHasModifierlevel(player, solidarytinkerModifiers.EXTREMELYCOLD_STATIC_MODIFIER.getId())) {
            if (event.getEntity().getType() == AetherEntityTypes.SUN_SPIRIT.get()) {
                event.getSource().bypassArmor().bypassMagic().bypassInvul();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onSunSpiritCool(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof Player player && ModifierLevel.EquipHasModifierlevel(player, solidarytinkerModifiers.EXTREMELYCOLD_STATIC_MODIFIER.getId())) {
            if (event.getEntity().getType() == AetherEntityTypes.SUN_SPIRIT.get()) {
                event.setAmount(event.getEntity().getMaxHealth() * 0.8F + event.getAmount());
            }
        }
    }
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
