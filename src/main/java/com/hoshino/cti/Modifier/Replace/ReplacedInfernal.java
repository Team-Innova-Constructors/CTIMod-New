package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplacedInfernal extends EtSTBaseModifier {
    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var target = context.getTarget();
        if (target instanceof LivingEntity living)
            damage+=(living.getHealth()>0.5f*living.getMaxHealth()?0.03f*living.getMaxHealth():-0.1f*living.getMaxHealth())*modifier.getLevel();
        return damage;
    }
}
