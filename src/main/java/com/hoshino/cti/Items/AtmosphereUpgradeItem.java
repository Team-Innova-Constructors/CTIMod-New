package com.hoshino.cti.Items;

import com.hoshino.cti.Blocks.BlockEntity.AtmosphereCondensatorEntity;
import com.hoshino.cti.Blocks.BlockEntity.AtmosphereExtractorEntity;
import com.hoshino.cti.Blocks.BlockEntity.GeneralMachineEntity;
import com.hoshino.cti.register.CtiTab;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class AtmosphereUpgradeItem extends TooltipedItem {
    public final float SPEED_FACTOR;
    public final float POWER_FACTOR;

    public AtmosphereUpgradeItem(float speedFactor, float powerFactor) {
        super(new Item.Properties().tab(CtiTab.MIXC).stacksTo(64), List.of(Component.translatable("cti.tooltip.item.atmosphere_upgrade").withStyle(ChatFormatting.GREEN), Component.translatable("cti.tooltip.item.speed_factor").append(" + " + String.format("%.1f", speedFactor) + " x").withStyle(ChatFormatting.AQUA), Component.translatable("cti.tooltip.item.power_factor").append(" * " + String.format("%.1f", powerFactor) + " x").withStyle(ChatFormatting.RED)));
        this.SPEED_FACTOR = speedFactor;
        this.POWER_FACTOR = powerFactor;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown() && !context.getLevel().isClientSide) {
            BlockEntity blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            ItemStack stack = context.getItemInHand();
            ItemStackHandler handler = null;
            if (blockEntity instanceof GeneralMachineEntity entity) handler = entity.getItemHandler();
            if (handler != null && (blockEntity instanceof AtmosphereExtractorEntity || blockEntity instanceof AtmosphereCondensatorEntity)) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    if (handler.getStackInSlot(i).isEmpty()) {
                        handler.setStackInSlot(i, new ItemStack(stack.getItem()));
                        stack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(context);
    }
}

