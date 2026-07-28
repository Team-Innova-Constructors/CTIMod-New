package com.hoshino.cti.util.method;

import com.xiaoyue.tinkers_ingenuity.register.TIItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.ArrayList;
import java.util.List;


public class GetModifierLevel {
    public static int getSlotModifierLevel(LivingEntity entity, ModifierId modifierId,EquipmentSlot slot) {
        if (entity instanceof Player) {
            var stack = entity.getItemBySlot(slot);
            if (stack.isEmpty() || !stack.is(TinkerTags.Items.MODIFIABLE)) return 0;
            ToolStack tool = ToolStack.from(stack);
            if (!tool.isBroken()) {
                return tool.getModifierLevel(modifierId);
            }
        }
        return 0;
    }

    public static int getEachHandsTotalModifierLevel(LivingEntity entity, ModifierId modifierId) {
        return getSlotModifierLevel(entity,modifierId,EquipmentSlot.MAINHAND)+getSlotModifierLevel(entity,modifierId,EquipmentSlot.OFFHAND);
    }
    public static int getTotalHandsModifierLevelWithShield(LivingEntity entity, ModifierId modifierId) {
        int mainHandLevel=0;
        int offHandLevel=0;
        if(entity.getMainHandItem().is(TinkerTags.Items.SHIELDS)){
            mainHandLevel=getSlotModifierLevel(entity,modifierId,EquipmentSlot.MAINHAND);
        }
        if(entity.getOffhandItem().is(TinkerTags.Items.SHIELDS)){
            offHandLevel=getSlotModifierLevel(entity,modifierId,EquipmentSlot.OFFHAND);
        }
        return mainHandLevel+offHandLevel;
    }

    public static boolean handsHaveModifierLevel(LivingEntity entity, ModifierId modifierId) {
        return getEachHandsTotalModifierLevel(entity, modifierId) > 0;
    }


    public static boolean inventoryHasThisModifier(LivingEntity entity, ModifierId modifierId) {
        if (entity instanceof Player player) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack.isEmpty() || !stack.is(TinkerTags.Items.MODIFIABLE))continue;
                if (ModifierUtil.getModifierLevel(stack, modifierId) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getTotalArmorModifierlevel(LivingEntity entity, ModifierId modifierId) {
        int total =0;
        if (entity instanceof Player player) {
            for (ItemStack stack : player.getInventory().armor) {
                if (stack.isEmpty() || !stack.is(TinkerTags.Items.MODIFIABLE))continue;
                var level=ModifierUtil.getModifierLevel(stack,modifierId);
                total +=level;
            }
        }
        return total;
    }

    public static int getAllSlotModifierLevel(LivingEntity entity, ModifierId modifierId) {
        return getTotalArmorModifierlevel(entity, modifierId) + getEachHandsTotalModifierLevel(entity, modifierId);
    }

    public static boolean eachArmorHasModifierLevel(LivingEntity entity, ModifierId modifierId) {
        return getSlotModifierLevel(entity, modifierId,EquipmentSlot.HEAD) > 0
                && getSlotModifierLevel(entity, modifierId,EquipmentSlot.CHEST) > 0
                && getSlotModifierLevel(entity, modifierId,EquipmentSlot.LEGS) > 0
                && getSlotModifierLevel(entity, modifierId,EquipmentSlot.FEET) > 0;
    }

    public static boolean equipHasModifierLevel(LivingEntity entity, ModifierId modifierId) {
        return getTotalArmorModifierlevel(entity, modifierId) > 0 || handsHaveModifierLevel(entity, modifierId);
    }

    public static List<Modifier> getCurioModifierInstanceList(Player player, ModifierId modifier) {
        List<ItemStack> list = new ArrayList<>();
        LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(player);
        if (handler.resolve().isPresent()) {
            for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                for (int i = 0; i < curios.getSlots(); ++i) {
                    ItemStack stack = curios.getStacks().getStackInSlot(i);
                    if (!stack.isEmpty() && stack.is(TinkerTags.Items.MODIFIABLE)) {
                        list.add(stack);
                    }
                }
            }
        }
        List<Modifier> modifierList = new ArrayList<>();
        for (ItemStack curio : list) {
            IToolStackView view = ToolStack.from(curio);
            Modifier ModifierInstance = view.getModifier(modifier).getModifier();
            modifierList.add(ModifierInstance);
        }
        return modifierList;
    }

    public static int curioModifierLevel(LivingEntity entity, ModifierId modifierId) {
        if (entity != null) {
            if (entity instanceof Player) {
                List<ItemStack> list = new ArrayList<>();
                LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosHelper().getCuriosHandler(entity);
                if (handler.resolve().isPresent()) {
                    for (ICurioStacksHandler curios : handler.resolve().get().getCurios().values()) {
                        for (int i = 0; i < curios.getSlots(); ++i) {
                            ItemStack stack = curios.getStacks().getStackInSlot(i);
                            if (!stack.isEmpty() && stack.is(TIItems.TINKER_RING.get())) {
                                list.add(stack);
                            }
                        }
                    }
                }
                for (ItemStack curios : list) {
                    return ModifierUtil.getModifierLevel(curios, modifierId);
                }
            }
        }
        return 0;
    }
    public static boolean curioHasModifierLevel(LivingEntity entity, ModifierId modifierId){
        return curioModifierLevel(entity,modifierId)>0;
    }

}
