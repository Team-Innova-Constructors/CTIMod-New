package com.hoshino.cti.Items;

import blusunrize.immersiveengineering.api.multiblocks.MultiblockHandler;
import flaxbeard.immersivepetroleum.api.event.ProjectorEvent;
import flaxbeard.immersivepetroleum.common.util.projector.MultiblockProjection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;

public class IEMultiblockGenerator extends Item {
    public IEMultiblockGenerator(Properties p_41383_,String multiBlockResourceLocation) {
        super(p_41383_);
        this.MultiBlockResourceLocation =new ResourceLocation(multiBlockResourceLocation);
    }
    private final ResourceLocation MultiBlockResourceLocation;
    public MultiblockHandler.IMultiblock getMultiblock(){
        return MultiblockHandler.getByUniqueName(MultiBlockResourceLocation);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();
        var pos = context.getClickedPos();
        var level = context.getLevel();
        if (player == null || level.getBlockState(pos).isAir() || stack.isEmpty()) {
            return InteractionResult.PASS;
        } else {
            player.startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (!(living instanceof ServerPlayer serverPlayer)) {
            return stack;
        }
        if (level.isClientSide) {
            return stack;
        }
        Rotation fixedRotation = Rotation.NONE;
        boolean fixedMirror = false;

        BlockHitResult rtr = getPlayerPOVHitResult(level, serverPlayer, ClipContext.Fluid.NONE);
        BlockPos pos = rtr.getBlockPos();
        Direction facing = rtr.getDirection();

        MultiblockHandler.IMultiblock multiblock = MultiblockHandler.getByUniqueName(MultiBlockResourceLocation);
        if (multiblock == null) {
            return stack;
        }

        BlockPos.MutableBlockPos hit = pos.mutable();
        BlockState state = level.getBlockState(pos);

        if (!state.isCollisionShapeFullBlock(level, pos) && facing == Direction.UP) {
            hit.move(0, 1, 0);
        }

        Vec3i size = multiblock.getSize(level);

        alignHit(hit, serverPlayer, size, fixedRotation, fixedMirror);

        BlockPos finalHitPos = hit.immutable();

        MultiblockProjection projection = new MultiblockProjection(level, multiblock);
        projection.setFlip(fixedMirror);
        projection.setRotation(fixedRotation);

        BiPredicate<Integer, MultiblockProjection.Info> pred = (layer, info) -> {
            BlockPos realPos = info.tPos.offset(finalHitPos);
            BlockState tstate0 = info.getModifiedState(level, realPos);
            ProjectorEvent.PlaceBlock event = new ProjectorEvent.PlaceBlock(
                    info.multiblock,
                    info.templateWorld,
                    info.tBlockInfo.pos,
                    level,
                    realPos,
                    tstate0,
                    fixedRotation
            );

            if (!MinecraftForge.EVENT_BUS.post(event)) {
                BlockState tstate1 = event.getState();
                if (level.setBlock(realPos, tstate1, 3)) {
                    ProjectorEvent.PlaceBlockPost postEvent = new ProjectorEvent.PlaceBlockPost(
                            info.multiblock,
                            info.templateWorld,
                            event.getTemplatePos(),
                            level,
                            realPos,
                            tstate1,
                            fixedRotation
                    );
                    MinecraftForge.EVENT_BUS.post(postEvent);
                }
            }
            return false;
        };
        projection.processAll(pred);
        stack.shrink(1);
        return stack;
    }
    private static void alignHit(BlockPos.MutableBlockPos hit, Player playerIn, Vec3i size, Rotation rotation, boolean mirror) {
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

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("长按右键使用可以直接在目标方块位置之上快速生成该多方块结构(还需工程师锤敲击)").withStyle(style -> style.withColor(0x9985ff)));
        p_41423_.add(Component.literal("如果用投影仪,在你使用投影仪的时候会尝试消耗你背包的该物品来直接按照预设形成结构").withStyle(style -> style.withColor(0xf6b2ff)));
    }
}
