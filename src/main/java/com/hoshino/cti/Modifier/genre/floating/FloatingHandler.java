package com.hoshino.cti.Modifier.genre.floating;

import com.hoshino.cti.content.materialGenre.GenreManager;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FloatingHandler extends Modifier implements MeleeDamageModifierHook, ProjectileHitModifierHook {
    public static int PRIORITY = 50;

    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE,ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        if (!context.getTarget().isOnGround())
            damage+= tool.getStats().get(GenreManager.FLOATING_GENRE.baseStat) + baseDamage*tool.getStats().get(GenreManager.FLOATING_GENRE.mulStat);
        return damage;
    }
}
