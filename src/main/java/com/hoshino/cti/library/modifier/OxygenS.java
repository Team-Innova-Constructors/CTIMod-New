package com.hoshino.cti.library.modifier;

import com.hoshino.cti.util.method.GetModifierLevel;
import net.minecraft.server.level.ServerPlayer;
import slimeknights.tconstruct.library.modifiers.ModifierId;

import java.util.Map;
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
        return true;
    }
}
