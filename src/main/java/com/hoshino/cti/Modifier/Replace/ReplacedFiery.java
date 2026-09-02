package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Modifier.genre.elemental.fiery.BasicBurntModifier;
import com.hoshino.cti.content.elementalSystem.ElementalDamageSource;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReplacedFiery extends BasicBurntModifier {
    @Override
    public int getMaxBurntBonus(IToolContext iToolContext, ModifierEntry modifierEntry) {
        return 10*modifierEntry.getLevel();
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity living){
            living.invulnerableTime = 0;
            living.hurt(ElementalDamageSource.fiery(context.getAttacker()),damage*0.2f*modifier.getLevel());
        }
    }
}
