package com.hoshino.cti.Modifier.genre.resourceConsuming.mana;

import com.hoshino.cti.Modifier.genre.resourceConsuming.mana.base.BasicBurstModifier;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.build.ModifierTraitModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;

import java.util.List;

public class EnchantedSwordModifier extends BasicBurstModifier {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addModule(new ModifierTraitModule(CtiModifiers.MANA_BURST_HANDLER.getId(),1,true));
    }

    @Override
    public void modifyBurst(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, ManaBurst burst, IManaBurstExtra burstExtras, ToolStack dummyLens) {
        burst.setColor(0x6F96FF);
        burst.setMana((int) (burst.getMana()*Math.pow(1.4,modifier.getLevel())));
        burstExtras.addBaseDamage(100);
        burstExtras.addDamageModifier(0.25f*modifier.getLevel());
        burst.entity().setDeltaMovement(burst.entity().getDeltaMovement().scale(Math.pow(1.4,modifier.getLevel())));
    }
}
