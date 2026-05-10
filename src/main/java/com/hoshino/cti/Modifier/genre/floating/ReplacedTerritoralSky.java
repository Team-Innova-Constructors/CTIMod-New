package com.hoshino.cti.Modifier.genre.floating;

import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.mining.ConditionalMiningSpeedModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;

public class ReplacedTerritoralSky extends BasicFloatingModifier{
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addModule(ConditionalMiningSpeedModule.builder().holder(LivingEntityPredicate.ON_GROUND.inverted()).amount(4,0));
    }

    @Override
    public float getMulBonus(IToolContext context, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*0.4f;
    }
}
