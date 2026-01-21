package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Items.MatrixReward;
import com.hoshino.cti.Items.SoyoKey;
import com.hoshino.cti.Items.SoyoKeyCategory;
import com.hoshino.cti.register.CtiBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BreakBlockPacket {
    private final BlockPos pos;
    private final int categoryIndex;

    public BreakBlockPacket(BlockPos pos, int categoryIndex) {
        this.pos = pos;
        this.categoryIndex = categoryIndex;
    }
    public BreakBlockPacket(FriendlyByteBuf buf){
       this.pos=buf.readBlockPos();
        this.categoryIndex=buf.readInt();
    }
    public void ToByte(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(categoryIndex);
    }
    public static void handle(BreakBlockPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level level = player.level;
            BlockPos pos = msg.pos;
            if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 && level.getBlockState(pos).is(CtiBlock.qi_yao_matrix.get())) {
                SoyoKeyCategory category = SoyoKeyCategory.values()[msg.categoryIndex];
                level.removeBlock(pos, false);
                for (MatrixReward reward : category.getReward()) {
                    ItemStack stack = reward.roll(player.getRandom());
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
                    itemEntity.setDeltaMovement(level.random.nextGaussian() * 0.05D, 0.2D, level.random.nextGaussian() * 0.05D);
                    level.addFreshEntity(itemEntity);
                }
                if (!player.getAbilities().instabuild) {
                    ItemStack mainHand = player.getMainHandItem();
                    if (mainHand.getItem() instanceof SoyoKey key && key.getSoyoKeyCategory() == category) {
                        mainHand.shrink(1);
                    }
                }
                level.levelEvent(2001, pos, Block.getId(CtiBlock.qi_yao_matrix.get().defaultBlockState()));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
