package com.hoshino.cti.Modifier.genre.elemental.common;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.register.CtiEntityTickers;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class PlasmaActivation extends EtSTBaseModifier implements IModifierWithSpecialDesc {
    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (context.getAttacker() instanceof Player player){
            var instance = EntityTickerManager.getInstance(player);
            List.of(CtiEntityTickers.FIERY_TOUCH_CD.get()).forEach(ticker -> {
                var tickerInstance = instance.getTicker(ticker);
                if (tickerInstance!=null){
                    if (tickerInstance.duration>modifier.getLevel()*5)
                        tickerInstance.duration-=modifier.getLevel()*5;
                    else instance.removeTicker(ticker);
                }
            });
        }
    }


    @Override
    public List<String> getDesc() {
        return List.of("info.cti.elemental_touch");
    }
}
