package com.hoshino.cti.Modifier.Race;

import com.hoshino.cti.Cti;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class DilapidatedMagic extends Modifier implements ModifyDamageModifierHook, OnAttackedModifierHook , InventoryTickModifierHook , TooltipModifierHook {
    private static final ResourceLocation COMPACT_TIME= Cti.getResource("compact_time_dilapidated");
    private static final ResourceLocation DAMAGE_AB_AMOUNT = Cti.getResource("dilapidated_damage_ab_amount");
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this,ModifierHooks.MODIFY_DAMAGE,ModifierHooks.ON_ATTACKED,ModifierHooks.INVENTORY_TICK,ModifierHooks.TOOLTIP);
    }
    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (damageSource.isMagic()) {
            amount *= 0.6f;
        }
        if(!damageSource.isMagic())return amount;
        var living = equipmentContext.getEntity();
        if (canProtect(tool, living)) {
            float lastAmount = getLastAmount(tool, living);
            float currentDamageAB = getDamageAB(tool);
            if(damageSource.isMagic()||damageSource.isExplosion()){
                amount/=4;
            }
            if (lastAmount > amount) {
                setDamageAbAmount(tool, currentDamageAB + amount);
                return 0;
            } else {
                setDamageAbAmount(tool, living.getMaxHealth() * 1.6f);
                living.level.playSound(null,living.getOnPos(), SoundEvents.ITEM_BREAK, SoundSource.AMBIENT,1,1.5f);
                return amount - lastAmount;
            }
        }
        return amount;
    }

    @Override
    public void onAttacked(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        setCooldown(iToolStackView, 15);
    }

    @Override
    public void onInventoryTick(IToolStackView iToolStackView, ModifierEntry modifierEntry, Level level, LivingEntity livingEntity, int i, boolean b, boolean b1, ItemStack itemStack) {
        if (livingEntity.tickCount % 20 != 0) return;
        int cooldown = getCooldown(iToolStackView);
        if (cooldown > 0) {
            setCooldown(iToolStackView, cooldown - 1);
        } else {
            if (getDamageAB(iToolStackView) > 0) {
                setDamageAbAmount(iToolStackView, 0);
            }
        }
    }
    private int getCooldown(IToolStackView view) {
        return view.getPersistentData().getInt(COMPACT_TIME);
    }

    private void setCooldown(IToolStackView view, int amount) {
        view.getPersistentData().putInt(COMPACT_TIME, amount);
    }
    private float getDamageAB(IToolStackView view) {
        return view.getPersistentData().getFloat(DAMAGE_AB_AMOUNT);
    }
    private float getLastAmount(IToolStackView view ,LivingEntity living){
        return living.getMaxHealth() * 1.6f-getDamageAB(view);
    }

    private void setDamageAbAmount(IToolStackView view, float amount) {
        view.getPersistentData().putFloat(DAMAGE_AB_AMOUNT, amount);
    }

    private boolean canProtect(IToolStackView view, LivingEntity living) {
        return !(getDamageAB(view) >= living.getMaxHealth() * 1.8f);
    }

    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if(player==null)return;
        float amount=getLastAmount(iToolStackView,player);
        list.add(Component.literal("败魔护盾剩余量:"+amount));
    }
}
