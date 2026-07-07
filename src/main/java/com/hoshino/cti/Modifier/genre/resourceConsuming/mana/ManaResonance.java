package com.hoshino.cti.Modifier.genre.resourceConsuming.mana;

import com.hoshino.cti.Cti;
import com.hoshino.cti.Modifier.genre.resourceConsuming.mana.base.BasicBurstModifier;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.VolatileDataModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class ManaResonance extends BasicBurstModifier implements VolatileDataModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.VOLATILE_DATA);
        hookBuilder.addModule(new ModifierTraitModule(CtiModifiers.MANA_BURST_HANDLER.getId(),1,true));
    }

    public static final ResourceLocation KEY_MANA_RESONANCE = Cti.getResource("mana_resonance");
    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burst.setColor(0xE679FF);
        burst.setMana(burst.getMana()+25);
    }

    @Override
    public void addVolatileData(IToolContext context, ModifierEntry modifier, ModDataNBT volatileData) {
        volatileData.putBoolean(KEY_MANA_RESONANCE,true);
    }
}
