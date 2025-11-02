package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Blocks.BlockEntity.GeneralMachineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PMachineFluidSync {
    private final FluidStack stack;
    private final BlockPos blockPos;

    public PMachineFluidSync(FluidStack stack, BlockPos blockPos) {
        this.stack = stack;
        this.blockPos = blockPos;
    }

    public PMachineFluidSync(FriendlyByteBuf buf) {
        this.stack = buf.readFluidStack();
        this.blockPos = buf.readBlockPos();
    }

    public void toByte(FriendlyByteBuf buf) {
        buf.writeFluidStack(this.stack);
        buf.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                if (Minecraft.getInstance().level.getBlockEntity(blockPos) instanceof GeneralMachineEntity entity) {
                    entity.setFluidDis(stack);
                }
            }
        });
        return true;
    }
}
