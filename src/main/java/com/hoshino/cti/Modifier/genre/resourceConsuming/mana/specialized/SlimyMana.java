package com.hoshino.cti.Modifier.genre.resourceConsuming.mana.specialized;

import com.hoshino.cti.Modifier.genre.resourceConsuming.mana.base.SpecializedBurstModifier;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.marth7th.solidarytinker.util.compound.DynamicComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class SlimyMana extends SpecializedBurstModifier implements ModifyBurstModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiBotModifierHooks.MODIFY_BURST);
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burst.setMana(burst.getMana()+tool.getStats().getInt(OverslimeModifier.OVERSLIME_STAT)*2);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        return DynamicComponentUtil.scrollColorfulText.getColorfulText(getTranslationKey(),null,
                new int[]{0x8FFF91,0x8FFCFF,0xC78EFF,0xFFB889},10,150,true);
    }
}
