package com.hoshino.cti.client.cache;

import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SevenCurse {
    @Getter
    @Setter
    public static int punishTime;
    @Getter
    @Setter
    public static int punishFre;
    @Getter
    @Setter
    public static int resoluteTime;
}
