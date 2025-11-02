package com.hoshino.cti.Modifier.Armor;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

public class SecondaryArmor extends EtSTBaseModifier {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_SECONDARY_ARMOR = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("secondary_armor"));

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ArmorLevelModule(KEY_SECONDARY_ARMOR,false,null));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().isBypassInvul()||!event.getSource().isBypassArmor()) return;
        LivingEntity living = event.getEntity();
        if (living instanceof Player player){
            living.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((cap)->{
                int level = cap.get(KEY_SECONDARY_ARMOR,0);
                if (level>0){
                    player.getInventory().hurtArmor(event.getSource(),event.getAmount(), Inventory.ALL_ARMOR_SLOTS);
                    event.setAmount(CombatRules.getDamageAfterAbsorb(event.getAmount(), (float) player.getArmorValue(), (float) player.getAttributeValue(Attributes.ARMOR_TOUGHNESS)));
                }
            });
        }
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int getPriority() {
        return 1000;
    }
}
