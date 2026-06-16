package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.Cti;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class NaturePower extends Modifier implements InventoryTickModifierHook , OnAttackedModifierHook ,ModifyDamageModifierHook{
    private static ResourceLocation NATURE_POWER= Cti.getResource("nature_power");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED,ModifierHooks.INVENTORY_TICK,ModifierHooks.MODIFY_DAMAGE);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if(livingEntity.tickCount%20!=0)return;
        var current=getNaturePower(iToolStackView);
        if(current>0){
            setNaturePower(iToolStackView,current-1);
        }
        if(getNaturePower(iToolStackView)>40){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,300,1,false,false,true));
        }
    }

    @Override
    public void onAttacked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(damageSource.isMagic()){
            var current=getNaturePower(iToolStackView);
            if(current<45){
                setNaturePower(iToolStackView,current+5);
            }
        }
    }
    private int getNaturePower(IToolStackView view){
        return view.getPersistentData().getInt(NATURE_POWER);
    }
    private void setNaturePower(IToolStackView view,int power){
        view.getPersistentData().putInt(NATURE_POWER,power);
    }

    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        if(getNaturePower(iToolStackView)>40&&damageSource.isMagic()){
            return 0.4f * v;
        }
        return v;
    }
}
