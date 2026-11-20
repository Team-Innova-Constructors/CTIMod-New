package com.hoshino.cti.content.materialGenre;

import java.util.Map;

public class GenreManager {
    public static final int FE_VALUE = 1;
    public static final int COMPRESSED_AIR_VALUE = 100;
    public static final int OVERSLIME_VALUE = 10000;
    public static final int DURABILITY_VALUE = 10000;

    public static final MaterialGenre.ResourceConsumingGenre OVERSLIME_GENRE = new MaterialGenre.ResourceConsumingGenre("overslime");
    public static final MaterialGenre INSATIABLE_GENRE = new MaterialGenre("insatiable");
    public static final MaterialGenre FLOATING_GENRE = new MaterialGenre("floating");
    public static final MaterialGenre BURNT_GENRE = new MaterialGenre("burnt");

    public static float calculateElementalRelease(int level,float val1,float val2){
        float percentage = Math.min(level,4f)*0.25f;
        return val1*(1-percentage)+val2*percentage;
    }
}
