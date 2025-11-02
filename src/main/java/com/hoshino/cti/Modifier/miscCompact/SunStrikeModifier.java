package com.hoshino.cti.Modifier.miscCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.hooks.PlasmaExplosionHitModifierHook;
import com.hoshino.cti.Entity.Projectiles.HomingSunStrike;
import com.hoshino.cti.register.CtiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SunStrikeModifier extends EtSTBaseModifier implements PlasmaExplosionHitModifierHook {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity){
            Level level = context.getAttacker().level;
            HomingSunStrike sunStrike = new HomingSunStrike(CtiEntity.HOMING_SUNSTRIKE.get(),level);
            sunStrike.owner = context.getAttacker();
            sunStrike.homingEntity = context.getLivingTarget();
            sunStrike.damage=damage*0.1f*(level.isDay()?2f:1);
            sunStrike.setStrikeCount(modifier.getLevel()-1);
            sunStrike.setPos(context.getTarget().position());
            level.addFreshEntity(sunStrike);
        }
    }
}
