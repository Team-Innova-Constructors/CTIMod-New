package com.hoshino.cti.Modifier;

import com.marth7th.solidarytinker.extend.superclass.BattleModifier;
import net.sixik.sdmshoprework.SDMShopR;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Unprincipled extends BattleModifier {
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var player=context.getPlayerAttacker();
        if(player==null)return damage;
        var coin= SDMShopR.getMoney(player);

        return damage * (1+0.05f * (coin / 100f));
    }
}
