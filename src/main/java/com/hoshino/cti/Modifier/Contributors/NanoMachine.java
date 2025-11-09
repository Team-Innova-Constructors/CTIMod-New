package com.hoshino.cti.Modifier.Contributors;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NanoMachine extends NoLevelsModifier implements ToolDamageModifierHook {
    @Override
    public int onDamageTool(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i, @Nullable LivingEntity livingEntity) {
        return 0;
    }
}
