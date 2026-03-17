package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.register.CtiModifiers;
import com.xiaoyue.tinkers_ingenuity.content.library.events.TinkerToolCriticalEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.tools.TinkerModifiers;

@Mod.EventBusSubscriber
public class Overcrit extends BasicOverslimeModifier {
    @SubscribeEvent
    public static void onToolCrit(TinkerToolCriticalEvent event){
        var os = TinkerModifiers.overslime.get();
        var tool = event.getTool();
        var osE = new ModifierEntry(os.getId(),1);
        if (!event.getCritical()&&tool.getModifierLevel(CtiModifiers.OVERCRIT.getId())>0&&os.getShield(tool)>5){
            event.setCritical(true);
            os.addOverslime(tool,osE,5);
        }
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return 50*modifier.getLevel();
    }

    @Override
    public String getDesc() {
        return "info.cti.overslime";
    }
}
