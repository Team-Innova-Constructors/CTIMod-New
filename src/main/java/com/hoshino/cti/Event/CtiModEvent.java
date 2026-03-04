package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CtiModEvent {
    @SubscribeEvent
    public static void addEntityAttributer(EntityAttributeModificationEvent event){
        event.add(EntityType.PLAYER, CtiAttributes.MAX_INSATIABLE.get());
    }
    @SubscribeEvent
    public static void onIMC(InterModEnqueueEvent event){
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,()->
                new SlotTypeMessage.Builder("slime_can").size(1).icon(Cti.getResource("slots/slime_can_slot")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE,()->
                new SlotTypeMessage.Builder("oxygen_can").size(1).icon(Cti.getResource("slots/oxygen_can_slot")).build());
    }
}
