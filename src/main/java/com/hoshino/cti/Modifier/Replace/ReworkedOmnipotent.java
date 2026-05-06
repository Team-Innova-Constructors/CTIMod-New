package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.register.CtiModifiers;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class ReworkedOmnipotent extends NoLevelsModifier implements ModifierTraitHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        if (firstEncounter){
            var statsIdOptional = context.getHook(ToolHooks.TOOL_MATERIALS).getStatTypes(context.getDefinition()).stream().findFirst();
            statsIdOptional.ifPresent(materialStatsId -> context.getMaterials().getList().stream().findFirst().ifPresent(material -> {
                MaterialRegistry.getInstance().getTraits(material.getId(), materialStatsId).forEach(entry->{
                    if (entry.getModifier()!= CtiModifiers.OMNIPOTENT.get())
                        builder.add(new ModifierEntry(entry.getId(),entry.getLevel()*2));
                });
            }));
        }
    }
}
