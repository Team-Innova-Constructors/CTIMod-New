package com.hoshino.cti.Event;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Items.SlimeCanItem;
import com.hoshino.cti.register.CtiAttributes;
import com.hoshino.cti.register.CtiItem;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.capability.ICurio;

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
