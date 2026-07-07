package com.hoshino.cti.Modifier.genre.resourceConsuming.mana;

import com.hoshino.cti.Modifier.genre.resourceConsuming.mana.base.BasicBurstModifier;
import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.ModifyBurstModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class TerraBurst extends BasicBurstModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ModifierTraitModule(CtiModifiers.MANA_BURST_HANDLER.getId(),1,true));
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burstExtras.addBaseDamage(20*modifier.getLevel());
        burstExtras.addDamageModifier(0.25f);
        burst.setMana(burst.getMana()+50*modifier.getLevel());
        burstExtras.addEntityPerConsumption(25);
        burstExtras.addBlockPerConsumption(25);
    }

    @Override
    public void burstLaunch(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, Player player, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        player.heal(2*modifier.getLevel());
    }
}
