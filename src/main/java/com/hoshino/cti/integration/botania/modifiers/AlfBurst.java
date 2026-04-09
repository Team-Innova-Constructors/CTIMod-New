package com.hoshino.cti.integration.botania.modifiers;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class AlfBurst extends NoLevelsModifier implements ModifyBurstModifierHook {
    @Override
    public int getPriority() {
        return 80;
    }
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiBotModifierHooks.MODIFY_BURST);
        hookBuilder.addModule(new ModifierTraitModule(CtiModifiers.MANA_BURST_HANDLER.getId(),1,true));
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burstExtras.addBaseDamage(8*modifier.getLevel());
        burstExtras.addEntityPerConsumption(-50);
        burstExtras.addBlockPerConsumption(-50);
        burst.setMana(burst.getMana()+50*modifier.getLevel());
        burst.setManaLossPerTick(burst.getManaLossPerTick()/2);
        burst.setColor(0xF79100);
    }
}
