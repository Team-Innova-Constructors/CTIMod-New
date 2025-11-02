package com.hoshino.cti.library.modifier.hooks;

import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PStackedOnMeC2S;
import com.hoshino.cti.netwrok.packet.PStackedOnOtherC2S;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collection;

public interface SlotStackModifierHook {
    default boolean overrideStackedOnOther(IToolStackView heldTool, ModifierEntry modifier, Slot slot, Player player) {
        return false;
    }

    default boolean overrideOtherStackedOnMe(IToolStackView slotTool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
        return false;
    }

    static boolean handleSlotStackOnMe(ItemStack slotStack,ItemStack held, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (slotStack.getItem() instanceof IModifiable&&action == ClickAction.SECONDARY && slotStack.getCount() == 1 && slot.allowModification(player)) {
            ToolStack tool = ToolStack.from(slotStack);
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getHook(CtiModifierHook.SLOT_STACK).overrideOtherStackedOnMe(tool, entry, held, slot, player, access)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean handleSlotStackOnOther(ItemStack stack ,Slot slot, ClickAction action, Player player){
        if (stack.getItem() instanceof IModifiable&&action == ClickAction.SECONDARY && stack.getCount() == 1 && slot.allowModification(player)) {
            ToolStack tool = ToolStack.from(stack);
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getHook(CtiModifierHook.SLOT_STACK).overrideStackedOnOther(tool, entry, slot, player)) {
                    return true;
                }
            }
        }
        return false;
    }

    record FirstMerger(Collection<SlotStackModifierHook> modules) implements SlotStackModifierHook {
        @Override
        public boolean overrideStackedOnOther(IToolStackView heldTool, ModifierEntry modifier, Slot slot, Player player) {
            for (SlotStackModifierHook module : modules) {
                if (module.overrideStackedOnOther(heldTool, modifier, slot, player)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean overrideOtherStackedOnMe(IToolStackView slotTool, ModifierEntry modifier, ItemStack held, Slot slot, Player player, SlotAccess access) {
            for (SlotStackModifierHook module : modules) {
                if (module.overrideOtherStackedOnMe(slotTool, modifier, held, slot, player, access)) {
                    return true;
                }
            }
            return false;
        }
    }
}
