package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.api.interfaces.IMachineAirHandlerProvider;
import com.hoshino.cti.netwrok.CtiPacketHandler;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.capabilities.MachineAirHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.common.network.TinkerNetwork;

import java.util.function.Supplier;

public class PAirHandlerSyncS2C {
    public final CompoundTag airHandlerNbt;
    public final BlockPos blockPos;

    public PAirHandlerSyncS2C(IAirHandlerMachine airHandler, BlockPos blockPos) {
        this.airHandlerNbt = airHandler.serializeNBT();
        this.blockPos = blockPos;
    }
    public PAirHandlerSyncS2C(FriendlyByteBuf buf){
        this.airHandlerNbt = buf.readNbt();
        this.blockPos = buf.readBlockPos();
    }
    public void encode(FriendlyByteBuf buf){
        buf.writeNbt(airHandlerNbt);
        buf.writeBlockPos(blockPos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier){
        var level = SafeClientAccess.getLevel();
        if (level!=null){
            var be = level.getBlockEntity(blockPos);
            if (be instanceof IMachineAirHandlerProvider provider){
                provider.getAirHandler().deserializeNBT(airHandlerNbt);
            }
        }
    }

    public static void syncAirToClient(BlockEntity blockEntity){
        if (blockEntity instanceof IMachineAirHandlerProvider provider&&blockEntity.getLevel() instanceof ServerLevel serverLevel){
            LevelChunk chunk = serverLevel.getChunkAt(blockEntity.getBlockPos());
            CtiPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk),
                    new PAirHandlerSyncS2C(provider.getAirHandler(),blockEntity.getBlockPos()));
        }
    }
}
