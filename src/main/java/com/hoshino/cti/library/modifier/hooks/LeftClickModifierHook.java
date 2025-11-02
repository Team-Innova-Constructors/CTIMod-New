package com.hoshino.cti.library.modifier.hooks;


import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import com.hoshino.cti.netwrok.packet.PLeftClickEmptyC2S;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collection;

public interface LeftClickModifierHook {
    default void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level , EquipmentSlot equipmentSlot){}
    default void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level , EquipmentSlot equipmentSlot, BlockState state, BlockPos pos){}

    static void handleLeftClick(ItemStack stack,Player player,EquipmentSlot slot){
        Level level = player.level;
        IToolStackView tool = ToolStack.from(stack);
        for (ModifierEntry entry:tool.getModifierList()){
            entry.getHook(CtiModifierHook.LEFT_CLICK).onLeftClickEmpty(tool,entry,player,level,slot);
        }
        if (level.isClientSide){
            CtiPacketHandler.INSTANCE.sendToServer(new PLeftClickEmptyC2S());
        }
    }
    static void handleLeftClickBlock(ItemStack stack,Player player,EquipmentSlot slot,BlockState state,BlockPos pos){
        Level level = player.level;
        IToolStackView tool = ToolStack.from(stack);
        for (ModifierEntry entry:tool.getModifierList()){
            entry.getHook(CtiModifierHook.LEFT_CLICK).onLeftClickBlock(tool,entry,player,level,slot,state,pos);
        }
    }
    record AllMerger(Collection<LeftClickModifierHook> modules) implements LeftClickModifierHook {
        @Override
        public void onLeftClickEmpty(IToolStackView tool, ModifierEntry entry, Player player, Level level , EquipmentSlot equipmentSlot) {
            for (LeftClickModifierHook module:this.modules){
                module.onLeftClickEmpty(tool,entry,player,level,equipmentSlot);
            }
        }
        @Override
        public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level , EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
            for (LeftClickModifierHook module:this.modules){
                module.onLeftClickBlock(tool,entry,player,level,equipmentSlot,state,pos);
            }
        }
    }

}

