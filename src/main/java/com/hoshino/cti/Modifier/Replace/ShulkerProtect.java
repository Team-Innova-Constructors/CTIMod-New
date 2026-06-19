package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.util.CommonUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.ToolDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.function.BiConsumer;

public class ShulkerProtect extends Modifier implements AttributesModifierHook, ModifyDamageModifierHook , ToolDamageModifierHook , ToolStatsModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_HURT,ModifierHooks.ATTRIBUTES,ModifierHooks.TOOL_DAMAGE,ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        var attModifier=new AttributeModifier(CommonUtil.UUIDFromSlot(slot,this.getId()), Attributes.MAX_HEALTH.getDescriptionId(), 0.1f * modifier.getLevel(), AttributeModifier.Operation.MULTIPLY_BASE);
        var attModifier1=new AttributeModifier(CommonUtil.UUIDFromSlotAndAttribute(slot,this.getId(),Attributes.ARMOR), Attributes.ARMOR.getDescriptionId(), 0.25f * modifier.getLevel(), AttributeModifier.Operation.MULTIPLY_BASE);
        consumer.accept(Attributes.MAX_HEALTH,attModifier);
        consumer.accept(Attributes.ARMOR,attModifier1);
    }


    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if(source.isExplosion()){
            amount/=0.6f;
        }
        if(source.isBypassMagic()){
            return amount *0.93f;
        }
        if(source.isBypassInvul())return amount;
        return amount * 0.85f;
    }

    @Override
    public int onDamageTool(IToolStackView iToolStackView, ModifierEntry modifierEntry, int i, @Nullable LivingEntity livingEntity) {
        return Math.min(10,(int)(i * 0.1f));
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        ToolStats.DURABILITY.add(modifierStatsBuilder,2000 * modifierEntry.getLevel());
    }
}
