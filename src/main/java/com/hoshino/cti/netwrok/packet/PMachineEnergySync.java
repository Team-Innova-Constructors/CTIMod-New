package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Blocks.BlockEntity.GeneralMachineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PMachineEnergySync {
    private final int energyStorage;
    private final BlockPos blockPos;

    public PMachineEnergySync(int amount, BlockPos blockPos) {
        this.energyStorage = amount;
        this.blockPos = blockPos;
    }

    public PMachineEnergySync(FriendlyByteBuf buf) {
        this.energyStorage = buf.readInt();
        this.blockPos = buf.readBlockPos();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeInt(this.energyStorage);
        buf.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                if (Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof GeneralMachineEntity entity) {
                    entity.setEnergy(energyStorage);
                }
            }
        });
        return true;
    }
}
