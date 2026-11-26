package com.hoshino.cti.Modifier.genre.elemental.fiery;

import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.hoshino.cti.util.EntityUtil.inflictBurnt;

public class CriticalBoostBurnt extends BasicBurntModifier {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (!context.isCritical() || !(context.getAttacker() instanceof Player player) || !(context.getTarget() instanceof LivingEntity target)) return;
        inflictBurnt(player, target, tool, 10f * modifier.getLevel());
        EntityTickerManager.getInstance(target).addTickerSimple(new EntityTickerInstance(CtiEntityTickers.CRITICAL_BOOST_BURNT.get(), modifier.getLevel(), 200 * modifier.getLevel()));
    }
}
