package com.hoshino.cti.Modifier.Meme;

import net.sixik.sdmshoprework.SDMShopR;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Fbfq extends Modifier implements MeleeDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        var p=context.getPlayerAttacker();
        var feima=tool.getPersistentData().getInt(Feima.FEIMA);
        if(feima%3==0)return 1;
        if(p==null)return damage;
        var money= SDMShopR.getMoney(p);
        if(money>1000){
            return damage + (baseDamage * Math.min(0.88f,0.0001f * ((money - 1000) /8f) * modifier.getLevel()));
        }
        return damage;
    }
}
