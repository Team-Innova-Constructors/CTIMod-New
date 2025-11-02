package com.hoshino.cti.util;

import earth.terrarium.ad_astra.common.data.Planet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class PlanetUtil {
    public static String getPressureDisplay(Planet planet) {
        ResourceKey<Level> level = planet.level();
        if (level == DimensionConstants.URANUS) {
            return "§c 1.80";
        } else if (level == DimensionConstants.JUPITER) {
            return "§c 3.25";
        }
        return "§a 无";
    }

    public static String getFreezeDisplay(Planet planet) {
        ResourceKey<Level> level = planet.level();
        if (level == DimensionConstants.URANUS) {
            return "§c 1.90";
        } else if (level == DimensionConstants.IONIZED_GLACIO) {
            return "§c 1.10~3.90";
        }
        return "§a 无";
    }

    public static String getIonizeDisplay(Planet planet) {
        ResourceKey<Level> level = planet.level();
        if (level == DimensionConstants.IONIZED_GLACIO) {
            return "§c 0.80~3.50";
        } else return "§a 无";
    }

    public static String getScorchDisplay(Planet planet) {
        ResourceKey<Level> level = planet.level();
        if (level == DimensionConstants.JUPITER) {
            return "§c 3.80";
        } else if (level == DimensionConstants.INFERNAL) {
            return "§c 2.10~3.50";
        }
        return "§a 无";
    }

}
