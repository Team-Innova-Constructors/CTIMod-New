package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class QuantumEntangle extends EtSTBaseModifier {

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (context.getMaterials().size()>0){
            var def = context.getDefinition();
            var materials = context.getMaterials().getList();
            var parts = def.getHook(ToolHooks.TOOL_PARTS).getParts(def);
            for (int i=0;i<parts.size()&&i<materials.size();i++){
                var statType = parts.get(i).getStatType();
                var material = materials.get(i).getId();
                var materialReg = MaterialRegistry.getInstance();
                if (statType== HeadMaterialStats.ID)
                    materialReg.getMaterialStats(material, HandleMaterialStats.ID).ifPresent(stat->
                            stat.apply(builder,0.5f));
                else if (statType==HandleMaterialStats.ID)
                    materialReg.getMaterialStats(material, HeadMaterialStats.ID).ifPresent(stat->
                            stat.apply(builder,0.5f));
                else if (statType== StatlessMaterialStats.BINDING.getIdentifier()) {
                    materialReg.getMaterialStats(material, HeadMaterialStats.ID).ifPresent(stat ->
                            stat.apply(builder, 0.25f));
                    materialReg.getMaterialStats(material, HandleMaterialStats.ID).ifPresent(stat ->
                            stat.apply(builder, 0.25f));
                } else {
                    materialReg.getMaterialStats(material, HeadMaterialStats.ID).ifPresent(stat ->
                            stat.apply(builder, 1f));
                    materialReg.getMaterialStats(material, HandleMaterialStats.ID).ifPresent(stat ->
                            stat.apply(builder, 1f));
                }
            }
        }
    }
}
