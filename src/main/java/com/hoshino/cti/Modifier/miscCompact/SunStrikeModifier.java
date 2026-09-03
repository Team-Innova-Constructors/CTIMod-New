package com.hoshino.cti.Modifier.miscCompact;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.c2h6s.etshtinker.hooks.PlasmaExplosionHitModifierHook;
import com.hoshino.cti.Entity.Projectiles.HomingSunStrike;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntity;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class SunStrikeModifier extends EtSTBaseModifier implements PlasmaExplosionHitModifierHook {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.isFullyCharged()&&context.getTarget() instanceof LivingEntity living&&context.getAttacker() instanceof Player player){
            Level level = context.getAttacker().level;
            int strikeCount = modifier.getLevel()-1;
            var fiery = EntityTickerManager.getInstancePlayerSpecific(living,player.getUUID()).getTicker(CtiEntityTickers.FIERY.get());
            if (fiery!=null)
                strikeCount++;
            HomingSunStrike sunStrike = new HomingSunStrike(CtiEntity.HOMING_SUNSTRIKE.get(),level);
            sunStrike.owner = player;
            sunStrike.homingEntity = context.getLivingTarget();
            sunStrike.damage=damage*0.2f;
            sunStrike.setStrikeCount(strikeCount);
            sunStrike.setPos(context.getTarget().position());
            level.addFreshEntity(sunStrike);
        }
    }
}
