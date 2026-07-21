package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.Cti;
import com.hoshino.cti.util.CommonUtil;
import com.james.tinkerscalibration.TinkersCalibration;
import com.marth7th.solidarytinker.util.MathUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.behavior.AttributesModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;
import java.util.function.BiConsumer;

public class AdvanceIgneous extends Modifier implements ModifyDamageModifierHook, AttributesModifierHook , InventoryTickModifierHook,TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ATTRIBUTES,ModifierHooks.MODIFY_HURT,ModifierHooks.INVENTORY_TICK,ModifierHooks.TOOLTIP);
    }

    private final ResourceLocation KEY = new ResourceLocation(Cti.MOD_ID, "igneous_point");
    @Override
    public float modifyDamageTaken(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentContext equipmentContext, EquipmentSlot equipmentSlot, DamageSource damageSource, float v, boolean b) {
        ModDataNBT persistentData = iToolStackView.getPersistentData();
        var currentTime=persistentData.getInt(KEY);
        var count=currentTime/10;
        var finalScale=1-(count/100);
        return (v * finalScale) - count;
    }

    @Override
    public void addAttributes(IToolStackView iToolStackView, ModifierEntry modifierEntry, EquipmentSlot equipmentSlot, BiConsumer<Attribute, AttributeModifier> biConsumer) {
        ModDataNBT persistentData = iToolStackView.getPersistentData();
        var currentTime=persistentData.getInt(KEY);
        var attModifier=new AttributeModifier(CommonUtil.UUIDFromSlot(equipmentSlot,this.getId()), "烈火融锻护甲加成", currentTime/10f , AttributeModifier.Operation.ADDITION);
        biConsumer.accept(Attributes.ARMOR,attModifier);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(!isCorrectSlot)return;
        if(!holder.isOnFire())return;
        if(world.isClientSide)return;
        var modifierLevel=modifier.getLevel();
        ModDataNBT persistentData = tool.getPersistentData();
        if(holder.tickCount%30!=0)return;
        var currentCount=persistentData.getInt(KEY);
        if(currentCount<=70 * modifierLevel){
            persistentData.putInt(KEY,currentCount+1);
        }
    }


    @Override
    public void addTooltip(IToolStackView iToolStackView, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT persistentData = iToolStackView.getPersistentData();
        var currentTime=persistentData.getInt(KEY);
        list.add(Component.literal("当前火成点数"+currentTime).withStyle(style -> style.withColor(this.getColor())));
    }
}
