package com.hoshino.cti.Modifier.aetherCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.technical.ArmorLevelModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;
@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class LifeEnhance extends EtSTBaseModifier implements AttributesModifierHook {
    public static final TinkerDataCapability.TinkerDataKey<Integer> KEY_LIFE_ENHANCE = TinkerDataCapability.TinkerDataKey.of(Cti.getResource("life_enhance_lv"));
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.ATTRIBUTES);
        builder.addModule(new ArmorLevelModule(KEY_LIFE_ENHANCE,false,null));
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(Attributes.MAX_HEALTH,new AttributeModifier(CommonUtil.UUIDFromSlot(slot,modifier.getId()),Attributes.MAX_HEALTH.getDescriptionId(),20, AttributeModifier.Operation.ADDITION));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingHeal(LivingHealEvent event){
        if (event.getEntity().getHealth()<event.getEntity().getMaxHealth()/2) {
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                if (cap.get(KEY_LIFE_ENHANCE, 0) > 0) {
                    event.setAmount(event.getAmount()*1.5f);
                }
            });
        }
    }
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if (event.getEntity().getHealth()>=event.getEntity().getMaxHealth()/2) {
            event.getEntity().getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap -> {
                if (cap.get(KEY_LIFE_ENHANCE, 0) > 0) {
                    event.setAmount(event.getAmount()*0.5f);
                }
            });
        }
    }
}
