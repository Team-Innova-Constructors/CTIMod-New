package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplacedInfernal extends EtSTBaseModifier {
    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var target = context.getLivingTarget();
        var player = context.getPlayerAttacker();
        if (target == null || player == null) return damage;
        if (!context.isFullyCharged()) return damage;
        int level = modifier.getLevel();
        if (target.getHealth() > 0.5f * target.getMaxHealth()) {
            float extraDamage = 0.03f * target.getMaxHealth();
            float maxBonus = 30.0f * level;
            damage += Math.min(extraDamage, maxBonus);
        } else {
            damage *= 0.7f;
        }

        return damage;
    }
}
