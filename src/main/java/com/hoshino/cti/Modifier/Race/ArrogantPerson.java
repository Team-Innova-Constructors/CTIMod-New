package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.Cti;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.function.BiConsumer;

public class ArrogantPerson extends Modifier implements AttributesModifierHook, InventoryTickModifierHook , OnAttackedModifierHook {
    private static ResourceLocation COMPACT_TIME= Cti.getResource("compact_time");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES,ModifierHooks.INVENTORY_TICK,ModifierHooks.ON_ATTACKED);
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        var attModifier=new AttributeModifier(CommonUtil.UUIDFromSlot(equipmentSlot,this.getId()), Attributes.MAX_HEALTH.getDescriptionId(), 12f * modifierEntry.getLevel(), AttributeModifier.Operation.ADDITION);
        biConsumer.accept(Attributes.MAX_HEALTH,attModifier);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(!b1)return;
        if(livingEntity.tickCount%20!=0)return;
        if(isNotCompacting(iToolStackView)&&livingEntity.getMaxHealth()>600){
            if(livingEntity.tickCount%100==0){
                livingEntity.heal(livingEntity.getMaxHealth() * 0.25f);
            }
        }
        if(getCompactSecond(iToolStackView)<7){
            setCompactSecond(iToolStackView, getCompactSecond(iToolStackView)+1);
        }
    }
    private int getCompactSecond(IToolStackView view){
        return view.getPersistentData().getInt(COMPACT_TIME);
    }
    private void setCompactSecond(IToolStackView view, int second){
        view.getPersistentData().putInt(COMPACT_TIME, second);
    }
    private boolean isNotCompacting(IToolStackView view){
        return getCompactSecond(view)>=7;
    }

    @Override
    public void onAttacked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(v>0){
            setCompactSecond(iToolStackView,0);
        }
    }
}
