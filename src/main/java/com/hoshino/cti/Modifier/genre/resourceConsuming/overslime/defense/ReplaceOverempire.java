package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.defense;

import com.hoshino.cti.Modifier.genre.resourceConsuming.overslime.base.BasicOverslimeModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;

public class ReplaceOverempire extends BasicOverslimeModifier implements ModifyDamageModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.MODIFY_HURT);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        var os = TinkerModifiers.overslime.get();
        if (os.getShield(tool)>=160){
            var bonus = os.getShield(tool)/160;
            bonus = Math.min(bonus,Math.min(20*modifier.getLevel(),60));
            os.addOverslime(tool,modifier,-5*bonus);
            amount-=amount*0.01f*bonus;
        }
        return amount;
    }
}
