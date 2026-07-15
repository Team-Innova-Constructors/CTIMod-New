package com.hoshino.cti.Modifier;

import com.hoshino.cti.Modifier.Base.EtchedModifier;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class GobberEtchIII extends EtchedModifier implements ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.ATTACK_DAMAGE.percent(builder,-0.05f);
        ToolStats.ATTACK_SPEED.percent(builder,-0.05f);
        ToolStats.DRAW_SPEED.percent(builder,-0.05f);
        ToolStats.PROJECTILE_DAMAGE.percent(builder,-0.05f);
        ToolStats.VELOCITY.percent(builder,-0.05f);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        var part = ItemStack.of(context.getPersistentData().getCompound(getId()));
        var modifiers = CommonUtil.getModifiersFromPart(part);
        if (!part.isEmpty()&&firstEncounter&&modifiers.size()>2){
            builder.add(modifiers.get(2));
        }
    }
}
