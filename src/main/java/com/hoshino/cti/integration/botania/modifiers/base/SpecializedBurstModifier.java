package com.hoshino.cti.integration.botania.modifiers.base;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class SpecializedBurstModifier extends EtSTBaseModifier implements ValidateModifierHook {
    @Override
    public int getPriority() {
        return -100;
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VALIDATE);
    }

    @Override
    public @Nullable Component validate(IToolStackView tool, ModifierEntry modifier) {
        int i = 0;
        List<Modifier> unsupported = new ArrayList<>();
        for (ModifierEntry entry:tool.getModifiers().getModifiers()){
            if (entry.getModifier() instanceof SpecializedBurstModifier){
                unsupported.add(entry.getModifier());
                i++;
            }
            if (i>1){
                var comp =Component.translatable("tooltip.cti.error.specialized_burst_modifier");
                for (int j=0;j<unsupported.size();j++){
                    if (j!=0) comp.append("、");
                    comp.append(unsupported.get(j).getDisplayName());
                }
                return comp;
            }
        }
        return null;
    }

    @Override
    public List<Component> getDescriptionList() {
        var list =new ArrayList<>(super.getDescriptionList());
        list.add(Component.translatable("info.cti.specialized_burst_modifier"));
        return list;
    }
}
