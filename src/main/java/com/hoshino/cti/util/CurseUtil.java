package com.hoshino.cti.util;

import com.aizistral.enigmaticlegacy.api.capabilities.IPlaytimeCounter;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CurseUtil {
    public static long curseTime(Player player) {
        IPlaytimeCounter counter = IPlaytimeCounter.get(player);
        return counter.getTimeWithCurses();
    }
    public static CompoundTag getCurseCurioData(Player player) {
        var handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        ItemStack curseCurio = null;
        if (handler.resolve().isPresent()) {
            for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                for (int i = 0; i < curios.getSlots(); ++i) {
                    ItemStack stack = curios.getStacks().getStackInSlot(i);
                    if (stack.is(EnigmaticItems.CURSED_RING)) {
                        curseCurio = stack;
                        break;
                    }
                }
            }
        }
        if (curseCurio != null) {
            return curseCurio.getOrCreateTag();
        }
        return null;
    }
    public static int getPunishTime(Player player) {
        var data=getCurseCurioData(player);
        if(data==null)return 0;
        return data.getInt("punish_time");
    }
    public static void setPunishTime(Player player,int day){
        var data=getCurseCurioData(player);
        if(data==null)return;
        data.putInt("punish_time",day * 300);
    }
    public static int getDeathFrequency(Player player) {
        var data=getCurseCurioData(player);
        if(data==null)return 0;
        return data.getInt("death_frequency");
    }
    public static void setDeathFrequency(Player player,int fre){
        var data=getCurseCurioData(player);
        if(data==null)return;
        data.putInt("death_frequency",fre);
    }
    public static int getResoluteTime(@NotNull Player player) {
        var data=getCurseCurioData(player);
        if(data==null)return 0;
        return data.getInt("resolute");
    }
    public static void setResoluteTime(Player player,int resoluteTime) {
        var data=getCurseCurioData(player);
        if(data==null)return;
        data.putInt("resolute", resoluteTime);
    }
    


}
