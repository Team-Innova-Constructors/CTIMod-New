package com.hoshino.cti.Modifier.Replace;

import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NerfColossal extends Modifier implements MeleeDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        float bonus = 0;
        if (context.getTarget() instanceof LivingEntity living && context.isFullyCharged() && context.getAttacker().getMaxHealth() > 0) {
            LivingEntity attacker = context.getAttacker();
            bonus = Math.min(100 * modifierEntry.getLevel(), baseDamage * (int) (living.getMaxHealth() / attacker.getMaxHealth()));
        }
        return damage + bonus;
    }
}
