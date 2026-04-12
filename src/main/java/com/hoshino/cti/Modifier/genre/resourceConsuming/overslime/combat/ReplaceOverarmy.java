package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.combat;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class ReplaceOverarmy extends BasicOverslimeModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onGetMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var os = TinkerModifiers.overslime.get();
        if (os.getShield(tool)>=100){
            var bonus = os.getShield(tool)/50;
            bonus = Math.min(bonus,200*modifier.getLevel());
            os.addOverslime(tool,modifier,-bonus);
            damage+=damage*0.01f*bonus;
        }
        return damage;
    }
}
