package com.hoshino.cti.register;

import com.hoshino.cti.Cti;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;


public class CtiToolStats {
    //抗性
    public static final FloatToolStat ELECTRIC_RESISTANCE = ToolStats.register(new FloatToolStat(name("electric_resistance"), -3135232, 0.0F, 0.0F, 5));
    public static final FloatToolStat SCORCH_RESISTANCE = ToolStats.register(new FloatToolStat(name("scorch_resistance"), -3135232, 0.0F, 0.0F, 5));
    public static final FloatToolStat FROZEN_RESISTANCE = ToolStats.register(new FloatToolStat(name("frozen_resistance"), -3135232, 0.0F, 0.0F, 5));
    public static final FloatToolStat PRESSURE_RESISTANCE = ToolStats.register(new FloatToolStat(name("pressure_resistance"), -3135232, 0.0F, 0.0F, 5));

    private static ToolStatId name(String name) {
        return new ToolStatId(Cti.MOD_ID, name);
    }
}
