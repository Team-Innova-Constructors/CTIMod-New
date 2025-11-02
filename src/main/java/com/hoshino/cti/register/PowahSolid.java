package com.hoshino.cti.register;

import com.marth7th.solidarytinker.register.solidarytinkerItem;
import owmii.powah.api.PowahAPI;

public class PowahSolid {
    public static void init() {
        PowahAPI.registerSolidCoolant(solidarytinkerItem.extremelycoldsteel_ingot.get(), 1981, -206);
    }
}
