package com.hoshino.cti.Modifier.genre.elemental.fiery;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class HighHeat extends BasicBurntModifier{
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return modifierEntry.getLevel()*5;
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        if (context.getTarget() instanceof LivingEntity living&&context.getAttacker() instanceof Player player&&context.isFullyCharged()){
            inflictBurnt(player,living,tool,modifier.getLevel());
        }
        return knockback;
    }
}
