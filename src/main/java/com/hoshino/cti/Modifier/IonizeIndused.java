package com.hoshino.cti.Modifier;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.content.environmentSystem.EDamageSource;
import com.hoshino.cti.content.environmentSystem.EnvironmentalHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class IonizeIndused extends EtSTBaseModifier {
    @Override
    public int getPriority() {
        return 255;
    }

    @Override
    public float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        Entity entity = context.getTarget();
        LivingEntity living = context.getAttacker();
        if (entity instanceof LivingEntity target && living instanceof Player player && !(target instanceof Player)&&context.isFullyCharged()) {
            target.invulnerableTime = 0;
            target.hurt(EDamageSource.indirectIonize(false, player,modifier.getLevel()), damage / 16);
            if (EnvironmentalHandler.getIonizeResistance(target) <= 1.5 &&EnvironmentalHandler.getIonizeValue(target) < 20) {
                EnvironmentalHandler.addIonizeValue(target, 4 * modifier.getLevel());
            }
            target.invulnerableTime = 0;
        }
        return knockback;
    }
}
