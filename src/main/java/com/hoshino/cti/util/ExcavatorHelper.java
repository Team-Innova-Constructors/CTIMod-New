package com.hoshino.cti.util;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import blusunrize.immersiveengineering.common.IESaveData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class ExcavatorHelper {
    public static void removeVein(ResourceKey<Level> dimension, MineralVein vein){
        synchronized(ExcavatorHandler.getMineralVeinList()){
            ExcavatorHandler.getMineralVeinList().remove(dimension,vein);
            ExcavatorHandler.resetCache();
            IESaveData.markInstanceDirty();
        }
    }
}
