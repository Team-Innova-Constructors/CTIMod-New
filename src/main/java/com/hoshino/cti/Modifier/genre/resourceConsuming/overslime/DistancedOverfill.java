package com.hoshino.cti.Modifier.genre.resourceConsuming.overslime;

import com.hoshino.cti.Cti;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.BlockInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public class DistancedOverfill extends NoLevelsModifier implements BlockInteractionModifierHook, TooltipModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.BLOCK_INTERACT,ModifierHooks.TOOLTIP);
    }

    public static final ResourceLocation KEY_X = Cti.getResource("overfill_x");
    public static final ResourceLocation KEY_Y = Cti.getResource("overfill_y");
    public static final ResourceLocation KEY_Z = Cti.getResource("overfill_z");
    public static final ResourceLocation KEY_DIM = Cti.getResource("overfill_dim");

    @Override
    public int getPriority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public InteractionResult beforeBlockUse(IToolStackView tool, ModifierEntry modifier, UseOnContext context, InteractionSource source) {
        Player player = context.getPlayer();
        if (player!=null&&player.isShiftKeyDown()) {
            BlockPos blockPos = context.getClickedPos();
            ModDataNBT nbt = tool.getPersistentData();
            BlockPos legacyPos = new BlockPos(nbt.getInt(KEY_X), nbt.getInt(KEY_Y), nbt.getInt(KEY_Z));
            BlockEntity be = context.getLevel().getBlockEntity(blockPos);
            if (blockPos.equals(legacyPos)&& context.getLevel().dimension().location().toString().equals(nbt.getString(KEY_DIM))){
                nbt.remove(KEY_X);
                nbt.remove(KEY_Y);
                nbt.remove(KEY_Z);
                nbt.remove(KEY_DIM);
                player.sendSystemMessage(Component.translatable("info.cti.container_disbound"));
                return InteractionResult.CONSUME;
            }
            if (!context.getLevel().isClientSide && be != null && be.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                tool.getPersistentData().putInt(KEY_X, blockPos.getX());
                tool.getPersistentData().putInt(KEY_Y, blockPos.getY());
                tool.getPersistentData().putInt(KEY_Z, blockPos.getZ());
                tool.getPersistentData().putString(KEY_DIM, context.getLevel().dimension().location().toString());
                player.sendSystemMessage(Component.translatable("info.cti.container_bind").append(" : ").append(blockPos.toShortString()).append(" @ ").append(Component.translatable(context.getLevel().dimension().location().toString())).withStyle(Style.EMPTY.withColor(0x07FF91)));
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (tool.getPersistentData().contains(KEY_DIM, Tag.TAG_STRING)) {
            ModDataNBT nbt = tool.getPersistentData();
            BlockPos blockPos = new BlockPos(nbt.getInt(KEY_X), nbt.getInt(KEY_Y), nbt.getInt(KEY_Z));
            String lang = tool.getPersistentData().getString(KEY_DIM);
            list.add(Component.translatable("info.cti.container_bind").append(" : ").append(blockPos.toShortString()).append(" @ ").append(Component.translatable(lang)));
        }
    }
}
