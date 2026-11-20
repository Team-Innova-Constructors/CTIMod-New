package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.Modifier.genre.elemental.ElementalRelease;
import com.hoshino.cti.content.materialGenre.GenreManager;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiToolStats;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
@Mod.EventBusSubscriber
public class BurntHandler extends Modifier implements TooltipModifierHook {
    @Override
    public int getPriority() {
        return 999;
    }

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this,ModifierHooks.TOOLTIP);
    }

//    @Override
//    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer){
//        if (slot==EquipmentSlot.OFFHAND&&!tool.hasTag(TinkerTags.Items.SHIELDS)) return;
//        consumer.accept(CtiAttributes.BURNT_INFLICT.get(),new AttributeModifier(
//                CommonUtil.UUIDFromSlot(slot,modifier.getId()),CtiAttributes.BURNT_INFLICT.get().getDescriptionId(),
//                tool.getStats().get(GenreManager.BURNT_GENRE.baseStat), AttributeModifier.Operation.ADDITION
//        ));
//        consumer.accept(CtiAttributes.BURNT_ATTACK_SPEED.get(),new AttributeModifier(
//                CommonUtil.UUIDFromSlot(slot,modifier.getId()),CtiAttributes.BURNT_ATTACK_SPEED.get().getDescriptionId(),
//                tool.getStats().get(GenreManager.BURNT_GENRE.mulStat), AttributeModifier.Operation.ADDITION
//        ));
//    }

    public static final UUID BURNT_INFLICT_UUID = UUID.fromString("dede6720-c228-fc01-53ac-2f47972b7aee");
    public static final UUID BURNT_ATKSPEED_UUID = UUID.fromString("55454cf2-4d46-842b-1cad-d681b4e8cfbf");
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEquipmentChange(LivingEquipmentChangeEvent event){
        if (event.getEntity() instanceof Player player&&CommonUtil.validateEquipmentChange(event.getFrom(),event.getTo())){
            var attributes = player.getAttributes();
            var instance1 = attributes.getInstance(CtiAttributes.BURNT_INFLICT.get());
            var instance2 = attributes.getInstance(CtiAttributes.BURNT_ATTACK_SPEED.get());
            float burntValue1 = 0;
            float burntValue2 = 0;
            float burntAtkSpValue1 = 0;
            float burntAtkSpValue2 = 0;
            for (EquipmentSlot slot:EquipmentSlot.values()){
                var itemStack = player.getItemBySlot(slot);
                if (CommonUtil.validateSlot(slot,itemStack)){
                    ToolStack toolStack = ToolStack.from(itemStack);
                    burntValue1+=toolStack.getStats().get(GenreManager.BURNT_GENRE.baseStat);
                    burntValue2+=toolStack.getStats().get(GenreManager.BURNT_GENRE.baseArmorStat);
                    burntAtkSpValue1+=toolStack.getStats().get(GenreManager.BURNT_GENRE.mulStat);
                    burntAtkSpValue2+=toolStack.getStats().get(GenreManager.BURNT_GENRE.mulArmorStat);
                }
            }
            AtomicInteger elementalReleaseLv = new AtomicInteger(0);
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
            if (instance1 !=null){
                if (instance1.getModifier(BURNT_INFLICT_UUID)!=null)
                    instance1.removeModifier(BURNT_INFLICT_UUID);
                instance1.addTransientModifier(new AttributeModifier(BURNT_INFLICT_UUID,"desc.cti.max_burnt",
                        GenreManager.calculateElementalRelease(elementalReleaseLv.get(),burntValue1,burntValue2), AttributeModifier.Operation.ADDITION));
            }
            if (instance2 !=null){
                if (instance2.getModifier(BURNT_ATKSPEED_UUID)!=null)
                    instance2.removeModifier(BURNT_ATKSPEED_UUID);
                instance2.addTransientModifier(new AttributeModifier(BURNT_ATKSPEED_UUID,"desc.cti.burnt_atkspeed",
                        GenreManager.calculateElementalRelease(elementalReleaseLv.get(),burntAtkSpValue1,burntAtkSpValue2), AttributeModifier.Operation.ADDITION));
            }
        }
    }


    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player!=null){
            AtomicInteger elementalReleaseLv = new AtomicInteger(0);
            player.getCapability(TinkerDataCapability.CAPABILITY).ifPresent(cap->elementalReleaseLv.set(cap.get(ElementalRelease.KEY_ELEMENTAL_RELEASE,0)));
            float burntValue = GenreManager.calculateElementalRelease(elementalReleaseLv.get(),tool.getStats().get(GenreManager.BURNT_GENRE.baseStat),tool.getStats().get(GenreManager.BURNT_GENRE.baseArmorStat));
            float burntAtkSpeedValue = GenreManager.calculateElementalRelease(elementalReleaseLv.get(),tool.getStats().get(GenreManager.BURNT_GENRE.mulStat),tool.getStats().get(GenreManager.BURNT_GENRE.mulArmorStat));
            tooltip.add(Component.literal((burntValue>=0?"+":"")+burntValue+" ").append(Component.translatable("attribute."+CtiAttributes.BURNT_INFLICT.getId().toLanguageKey())).withStyle(s->s.withColor(0xE8891A)));
            tooltip.add(Component.literal((burntAtkSpeedValue>=0?"+":"")+burntAtkSpeedValue+" ").append(Component.translatable("attribute."+ CtiAttributes.BURNT_ATTACK_SPEED.getId().toLanguageKey())).withStyle(s->s.withColor(0xE8891A)));
        }
    }
}
