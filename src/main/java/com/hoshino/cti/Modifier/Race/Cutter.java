package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class Cutter extends Modifier implements MeleeHitModifierHook{
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if(context.isFullyCharged()){
            var target=context.getLivingTarget();
            var attacker=context.getPlayerAttacker();
            if(target==null||attacker==null)return;
            var maxArmorAttr = target.getAttribute(Attributes.ARMOR);
            if (maxArmorAttr != null) {
                var modifierId = CommonUtil.UUIDFromAnyString("cutter");
                var nbt = target.getPersistentData();
                int currentStacks = nbt.getInt("CutterArmorDebuffStacks") + modifier.getLevel();
                nbt.putInt("CutterArmorDebuffStacks", currentStacks);
                double totalReduction = -0.06 * currentStacks;
                var attModifier = new AttributeModifier(modifierId, "cutter", totalReduction, AttributeModifier.Operation.MULTIPLY_TOTAL);
                if (maxArmorAttr.hasModifier(attModifier)) {
                    maxArmorAttr.removeModifier(attModifier);
                }
                maxArmorAttr.addTransientModifier(attModifier);
            }
        }
    }
}
