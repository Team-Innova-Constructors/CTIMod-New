package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Entity.Projectiles.StarDargonAmmo;
import com.hoshino.cti.Modifier.StarDargonHit;
import com.hoshino.cti.client.cache.SevenCurse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;
import java.util.function.Supplier;

public class StarHitPacket {
    private final UUID mobUUID;

    public StarHitPacket(UUID mobUUID) {
        this.mobUUID = mobUUID;
    }

    public StarHitPacket(FriendlyByteBuf buf) {
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
            if(mob==null)return;
            var view= ToolStack.from(sender.getMainHandItem());
            int starDust=view.getPersistentData().getInt(StarDargonHit.STAR_DUST);
            if(starDust>=10){
                view.getPersistentData().putInt(StarDargonHit.STAR_DUST,starDust-10);
                float damageShouldBe=StarDargonHit.DAMAGE_SHOULD_BE.getOrDefault(sender.getUUID(),10f);
                var ammo=new StarDargonAmmo(sender,sender.getLevel(),mob.blockPosition(),damageShouldBe);
                sender.getLevel().playSound(null,sender, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.AMBIENT,1f,1f);
                sender.getLevel().addFreshEntity(ammo);
            }
        });
        return true;
    }
}
