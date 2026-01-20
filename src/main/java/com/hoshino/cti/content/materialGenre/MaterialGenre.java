package com.hoshino.cti.content.materialGenre;

import com.hoshino.cti.Cti;
import net.minecraft.tags.TagKey;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MaterialGenre {
    private static ToolStatId name(String name) {
        return new ToolStatId(Cti.MOD_ID, name);
    }

    public final String ID;
    public final TagKey<IMaterial> materialTag;
    public final TagKey<Modifier> modifierTag;
    public final FloatToolStat baseStat;
    public final FloatToolStat mulStat;
    public final FloatToolStat baseArmorStat;
    public final FloatToolStat mulArmorStat;

    public MaterialGenre(String id) {
        ID = id;
        materialTag = MaterialManager.getTag(Cti.getResource(id));
        modifierTag = ModifierManager.getTag(Cti.getResource(id));
        baseStat = ToolStats.register(new FloatToolStat(name(id+"_base"),0x000000,0,0,Float.MAX_VALUE));
        mulStat = ToolStats.register(new FloatToolStat(name(id+"mul"),0x000000,0,0,Float.MAX_VALUE));
        baseArmorStat = ToolStats.register(new FloatToolStat(name(id+"_base_armor"),0x000000,0,0,1000));
        mulArmorStat = ToolStats.register(new FloatToolStat(name(id+"_mul_armor"),0x000000,0,0,0.4f));
    }

    public static class ResourceConsumingGenre extends MaterialGenre{
        public final FloatToolStat consumption;

        public ResourceConsumingGenre(String id) {
            super(id);
            this.consumption = ToolStats.register(new FloatToolStat(name(id+"_consumption"),0x000000,1,1,Integer.MAX_VALUE));
        }
    }
}
