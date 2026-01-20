package com.hoshino.cti.integration.botania.api.hook;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.api.interfaces.IModifierManaLensProvider;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.internal.ManaBurst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface LensProviderModifierHook {
    default List<ItemStack> getLens(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, ManaBurst burst, IManaBurstExtra burstExtra){
        return List.of();
    }
    static List<ItemStack> gatherLens(IToolStackView tool,ManaBurst burst){
        List<ItemStack> list = new ArrayList<>();
        tool.getModifierList().forEach(entry -> {
            if (entry.getModifier() instanceof IModifierManaLensProvider provider) list.addAll(provider.getLens());
            else list.addAll(entry.getHook(CtiBotModifierHooks.LENS_PROVIDER).getLens(tool,entry,tool.getModifierList(),burst,(IManaBurstExtra) burst));
        });
        return list;
    }
    record AllMerger(Collection<LensProviderModifierHook> modules) implements LensProviderModifierHook {
        @Override
        public List<ItemStack> getLens(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, ManaBurst burst,IManaBurstExtra burstExtra) {
            List<ItemStack> list = new ArrayList<>();
            for (var entry:this.modules){
                list.addAll(entry.getLens(tool,modifier,modifierList,burst,(IManaBurstExtra) burst));
            }
            return list;
        }
    }
}
