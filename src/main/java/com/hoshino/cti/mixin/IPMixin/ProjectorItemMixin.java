package com.hoshino.cti.mixin.IPMixin;

import blusunrize.immersiveengineering.api.tool.IUpgradeableTool;
import flaxbeard.immersivepetroleum.api.event.ProjectorEvent;
import flaxbeard.immersivepetroleum.common.items.IPItemBase;
import flaxbeard.immersivepetroleum.common.items.ProjectorItem;
import flaxbeard.immersivepetroleum.common.util.projector.MultiblockProjection;
import flaxbeard.immersivepetroleum.common.util.projector.Settings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import static flaxbeard.immersivepetroleum.common.items.ProjectorItem.getSettings;

@Mixin(ProjectorItem.class)
public abstract class ProjectorItemMixin extends IPItemBase implements IUpgradeableTool {
    @Unique
    private static void cti_new$alignHit(BlockPos.MutableBlockPos hit, Player playerIn, Vec3i size, Rotation rotation, boolean mirror) {
        int x = (rotation.ordinal() % 2 == 0 ? size.getX() : size.getZ()) / 2;
        int z = (rotation.ordinal() % 2 == 0 ? size.getZ() : size.getX()) / 2;
        Direction facing = playerIn.getDirection();
        boolean xEven = size.getX() % 2 == 0;
        boolean zEven = size.getZ() % 2 == 0;
        switch (facing) {
            case NORTH -> hit.setWithOffset(hit, 0, 0, -z + (zEven ? 1 : 0));
            case SOUTH -> hit.setWithOffset(hit, 0, 0, z);
            case EAST -> hit.setWithOffset(hit, x, 0, 0);
            case WEST -> hit.setWithOffset(hit, -x + (xEven ? 1 : 0), 0, 0);
        }
    }

    @Inject(method = "m_6225_", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;m_6144_()Z"), cancellable = true, remap = false)
    private void onUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        Player playerIn = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (playerIn == null) return;

        ItemStack stack = playerIn.getItemInHand(hand);
        Settings settings = getSettings(stack);

        if (playerIn.isCreative()) return;
        if (!(playerIn.isCrouching())) return;

        if (settings.getMode() == Settings.Mode.PROJECTION && settings.getPos() == null && settings.getMultiblock() != null) {
            BlockState state = world.getBlockState(pos);
            BlockPos.MutableBlockPos hit = pos.mutable();

            if (!state.getMaterial().isReplaceable() && facing == Direction.UP) {
                hit.setWithOffset(hit, 0, 1, 0);
            }

            Vec3i size = settings.getMultiblock().getSize(world);
            cti_new$alignHit(hit, playerIn, size, settings.getRotation(), settings.isMirrored());

            if (!world.isClientSide) {
                var targetMultiblock = settings.getMultiblock();
                Map<Item, Integer> requiredItems = new HashMap<>();
                MultiblockProjection projection = new MultiblockProjection(world, targetMultiblock);
                projection.setFlip(settings.isMirrored());
                projection.setRotation(settings.getRotation());

                projection.processAll((layer, info) -> {
                    BlockState requiredState = info.getModifiedState(world, info.tPos.offset(hit));
                    Item item = requiredState.getBlock().asItem();
                    if (item != Items.AIR) {
                        requiredItems.put(item, requiredItems.getOrDefault(item, 0) + 1);
                    }
                    return false;
                });
                boolean needMaterials=false;

                for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                    int countInInv = getCountInInventory(playerIn, entry.getKey());
                    if (countInInv < entry.getValue()) {
                        var level=playerIn.getLevel();
                        if(!level.isClientSide){
                            playerIn.sendSystemMessage(Component.literal("缺少材料: ").append(entry.getKey().getDescription()).append(" x" + (entry.getValue() - countInInv)));
                        }
                        needMaterials=true;
                    }
                }
                if(needMaterials)return;
                for (Map.Entry<Item, Integer> entry : requiredItems.entrySet()) {
                    removeItemFromInventory(playerIn, entry.getKey(), entry.getValue());
                }

                if (targetMultiblock.getUniqueName().getPath().contains("excavator_demo") ||
                        targetMultiblock.getUniqueName().getPath().contains("bucket_wheel")) {
                    hit.setWithOffset(hit, 0, -2, 0);
                }

                BiPredicate<Integer, MultiblockProjection.Info> pred = (layer, info) -> {
                    BlockPos realPos = info.tPos.offset(hit);
                    BlockState tstate0 = info.getModifiedState(world, realPos);
                    ProjectorEvent.PlaceBlock event = new ProjectorEvent.PlaceBlock(info.multiblock, info.templateWorld, info.tBlockInfo.pos, world, realPos, tstate0, settings.getRotation());

                    if (!MinecraftForge.EVENT_BUS.post(event)) {
                        BlockState tstate1 = event.getState();
                        if (world.setBlockAndUpdate(realPos, tstate1)) {
                            ProjectorEvent.PlaceBlockPost postEvent = new ProjectorEvent.PlaceBlockPost(info.multiblock, info.templateWorld, event.getTemplatePos(), world, realPos, tstate1, settings.getRotation());
                            MinecraftForge.EVENT_BUS.post(postEvent);
                        }
                    }
                    return false;
                };

                projection.processAll(pred);
            }
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Unique
    private int getCountInInventory(Player player, Item item) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.is(item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    @Unique
    private void removeItemFromInventory(Player player, Item item, int amount) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.is(item)) {
                int toRemove = Math.min(amount, stack.getCount());
                stack.shrink(toRemove);
                amount -= toRemove;
                if (amount <= 0) break;
            }
        }
    }
}
