package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Modifier.Contributors.Nkssdtt;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;
import java.util.function.Supplier;

public class NksszsPacket {
    private final UUID mobUUID;

    public NksszsPacket(UUID mobUUID) {
        this.mobUUID = mobUUID;
    }

    public NksszsPacket(FriendlyByteBuf buf) {
        this.mobUUID = buf.readUUID();
    }
    public void ToByte(FriendlyByteBuf buf) {
        buf.writeUUID(mobUUID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            var sender=context.getSender();
            if(sender==null)return;
            var serverLevel=sender.getLevel();
            var mob=serverLevel.getEntity(mobUUID);
            var view = ToolStack.from(sender.getMainHandItem());
            ModDataNBT data = view.getPersistentData();
            int waitSecond=view.getPersistentData().getInt(Nkssdtt.NKSSTK_COOLDOWN);
            if(waitSecond>0){
                sender.displayClientMessage(Component.literal("钢铁断头台还在冷却"+waitSecond).withStyle(style -> style.withColor(0xff0000)),true);
                return;
            }
            if (mob != null) {
                data.putString(Nkssdtt.NKSSTK, mob.getStringUUID());
                sender.getPersistentData().putInt("nksswait", 6);
                mob.getPersistentData().putInt("nksszs_reload",8);
                view.getPersistentData().putInt(Nkssdtt.NKSSTK_COOLDOWN,60);
            }
        });
        return true;
    }
}
