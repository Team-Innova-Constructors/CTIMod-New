package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.init.etshtinkerToolStats;
import com.c2h6s.etshtinker.tools.stats.PlasmaGeneratorMaterialStats;
import com.hoshino.cti.Modifier.Base.EtchedModifier;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class SlashColorize extends EtchedModifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        var stack = ItemStack.of(context.getPersistentData().getCompound(getId()));
        if (!stack.isEmpty()&&stack.getItem() instanceof IToolPart part){
            var statType = part.getStatType();
            var material = part.getMaterial(stack).getId();
            MaterialRegistry.getInstance().getMaterialStats(material,statType).ifPresent(iMaterialStats -> {
                if (iMaterialStats instanceof PlasmaGeneratorMaterialStats materialStats){
                    etshtinkerToolStats.SLASH_COLOR.add(builder,materialStats.slashColor() - builder.getStat(etshtinkerToolStats.SLASH_COLOR));
                }
            });
        }
    }
}
