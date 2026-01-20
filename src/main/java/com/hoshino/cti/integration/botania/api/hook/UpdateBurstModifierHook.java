package com.hoshino.cti.integration.botania.api.hook;

import com.hoshino.cti.api.interfaces.IToolProvider;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.lens.LensItem;

import java.util.Collection;
import java.util.List;

public interface UpdateBurstModifierHook {
    default void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtra){

    }

    static void handleBurstUpdate(ManaBurst burst, ItemStack stack){
        IToolStackView actualTool = ((IToolProvider)burst).cti$getTool();
        ToolStack toolStack = ToolStack.from(stack);
        toolStack.getModifierList().forEach(entry -> entry.getHook(CtiBotModifierHooks.UPDATE_BURST).updateBurst(actualTool,entry,toolStack.getModifierList(),burst.entity().getOwner(),burst,(IManaBurstExtra) burst));
        LensProviderModifierHook.gatherLens(toolStack,burst).forEach(lensStack ->{
            if (lensStack.getItem() instanceof LensItem lens) lens.updateBurst(burst,stack);
        });
    }

    record AllMerger(Collection<UpdateBurstModifierHook> modules) implements UpdateBurstModifierHook {
        @Override
        public void updateBurst(@Nullable IToolStackView tool,ModifierEntry modifier,List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtra) {
            this.modules.forEach(hook-> hook.updateBurst(tool,modifier,modifierList,owner,burst,(IManaBurstExtra) burst));
        }
    }
}
