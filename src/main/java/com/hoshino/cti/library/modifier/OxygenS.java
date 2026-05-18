package com.hoshino.cti.library.modifier;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.hoshino.cti.Capabilitiess.IFreezeShielding;
import com.hoshino.cti.Capabilitiess.ctiCapabilities;
import com.hoshino.cti.util.CtiTagkey;
import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OxygenS {
    public static final Map<UUID, Integer> OXYGEN_TICK_MAP = new ConcurrentHashMap<>();
    public static void checkAndRecoverOxygen(ServerPlayer serverPlayer){
        UUID uuid = serverPlayer.getUUID();
        int current = OxygenS.OXYGEN_TICK_MAP.getOrDefault(uuid, 0);
        if (current > 0) {
            int nextValue = (current > 140) ? 140 : current - 1;
            if (nextValue <= 0) {
                OxygenS.OXYGEN_TICK_MAP.remove(uuid);
            } else {
                OxygenS.OXYGEN_TICK_MAP.put(uuid, nextValue);
            }
        }
    }
    public static boolean checkNeedOxygen(ServerPlayer serverPlayer) {
        if(serverPlayer.isCreative())return false;
        ModifierId[] modifierIDs=new ModifierId[]{
                new ModifierId("cti:infinity"),
                new ModifierId("cti:starbless"),
                new ModifierId("cti:eventually"),
                new ModifierId("cti:all"),
        };

        for(ModifierId modifierId:modifierIDs){
            if(GetModifierLevel.EquipHasModifierlevel(serverPlayer,modifierId)){
                return false;
            }
        }
        if(SuperpositionHandler.hasCurio(serverPlayer, EnigmaticItems.ENIGMATIC_ITEM)||SuperpositionHandler.hasCurio(serverPlayer, EnigmaticItems.THE_CUBE)){
            return false;
        }
        for (ItemStack stack : serverPlayer.getInventory().armor) {
            if (stack.getTags().toList().contains(CtiTagkey.OXYGEN_REGEN)) {
                return false;
            }
        }
        return true;
    }
}
