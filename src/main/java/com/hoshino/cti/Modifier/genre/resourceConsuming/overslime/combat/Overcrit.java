package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import com.hoshino.cti.register.CtiModifiers;
import com.xiaoyue.tinkers_ingenuity.content.library.events.TinkerToolCriticalEvent;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.List;

public class Overcrit extends BasicOverslimeModifier {

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var os = TinkerModifiers.overslime.get();
        if (os.getShield(tool) > 5&&context.isCritical()) {
            damage+=baseDamage*0.5f*modifier.getLevel();
            os.addOverslime(tool, modifier, 5*modifier.getLevel());
        }
        return damage;
    }

    @Override
    public int getOverslimeBonus(IToolContext context, ModifierEntry modifier) {
        return 50*modifier.getLevel();
    }

}
