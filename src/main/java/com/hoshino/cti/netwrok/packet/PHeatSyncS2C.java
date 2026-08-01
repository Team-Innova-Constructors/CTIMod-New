package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.api.interfaces.IMachineHeatExchangerProvider;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import slimeknights.mantle.client.SafeClientAccess;

import java.util.function.Supplier;

/**
 * 服务端→客户端同步某个 {@link IMachineHeatExchangerProvider} 方块实体的热交换器 NBT。
 * 仅同步温度等数据，避免依赖 saveSynced 仅在方块更新时才发送。
 */
public class PHeatSyncS2C {
    public final CompoundTag heatNbt;
    public final BlockPos blockPos;

    public PHeatSyncS2C(CompoundTag heatNbt, BlockPos blockPos) {
        this.heatNbt = heatNbt;
        this.blockPos = blockPos;
    }

    public PHeatSyncS2C(FriendlyByteBuf buf) {
        this.heatNbt = buf.readNbt();
        this.blockPos = buf.readBlockPos();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(heatNbt);
        buf.writeBlockPos(blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        var level = SafeClientAccess.getLevel();
        if (level != null && heatNbt != null) {
            var be = level.getBlockEntity(blockPos);
            if (be instanceof IMachineHeatExchangerProvider provider) {
                provider.getHeatExchanger().deserializeNBT(heatNbt);
            }
        }
    }

    public static void syncHeatToClient(BlockEntity blockEntity) {
        if (blockEntity instanceof IMachineHeatExchangerProvider provider
                && blockEntity.getLevel() instanceof ServerLevel serverLevel) {
            LevelChunk chunk = serverLevel.getChunkAt(blockEntity.getBlockPos());
            CtiPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new PHeatSyncS2C(provider.getHeatExchanger().serializeNBT(), blockEntity.getBlockPos()));
        }
    }
}