package com.hoshino.cti.Modifier.Replace;

import com.xiaoyue.tinkers_ingenuity.generic.XIModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SharpBlade extends XIModifier {
    public SharpBlade() {
    }

    public boolean isSingleLevel() {
        return true;
    }

    private float getBonus(IToolStackView tool, LivingEntity target) {
        float attackDamage = tool.getStats().get(ToolStats.ATTACK_DAMAGE);
        return (attackDamage * 0.3F * target.getMaxHealth() * 0.15F) * 0.1F;
    }

    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return actualDamage + Math.min(this.getBonus(tool, target), target.getMaxHealth() * 0.1F);
    }
}
