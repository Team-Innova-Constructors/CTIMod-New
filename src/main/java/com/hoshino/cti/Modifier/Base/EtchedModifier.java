package com.hoshino.cti.Modifier.Base;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class EtchedModifier extends NoLevelsModifier implements ModifierTraitHook {
    @Override
    public int getPriority() {
        return Integer.MAX_VALUE-10;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MODIFIER_TRAITS);
    }

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        var part = ItemStack.of(context.getPersistentData().getCompound(getId()));
        if (!part.isEmpty()&&firstEncounter){
            builder.add(CommonUtil.getModifiersFromPart(part));
        }
    }

    @Override
    public Component getDisplayName(IToolStackView tool, ModifierEntry entry) {
        var part = ItemStack.of(tool.getPersistentData().getCompound(getId()));
        if (part.isEmpty()) return super.getDisplayName().copy().append(" 刻失败");
        return super.getDisplayName().copy().append("  刻印物:").append(part.getDisplayName());
    }
}
