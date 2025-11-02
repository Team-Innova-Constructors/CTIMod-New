package com.hoshino.cti.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AdvanceMentHelper {
    public static boolean hasCompletedAdvancement(ServerPlayer player, ResourceLocation advancementResourceLocationID){
        if(player.getServer()==null)return false;
        var advancement=player.getServer().getAdvancements().getAdvancement(advancementResourceLocationID);
        if(advancement==null)return false;
        var progress= player.getAdvancements().getOrStartProgress(advancement);
        return progress.isDone();
    }
}
