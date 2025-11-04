package com.hoshino.cti.netwrok.packet;

import com.hoshino.cti.Entity.Projectiles.StarDargonAmmo;
import com.hoshino.cti.Modifier.StarDargonHit;
import com.hoshino.cti.client.cache.SevenCurse;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
            int waitTick=StarDargonHit.getFreezeTick(view);
            if(waitTick>0){
                sender.displayClientMessage(Component.literal("还在冷却,冷却时长为"+waitTick+"秒").withStyle(style -> style.withColor(0x64a9fd)),true);
                return;
            }
            if(starDust>=50){
                view.getPersistentData().putInt(StarDargonHit.STAR_DUST,starDust-50);
                float damageShouldBe=StarDargonHit.DAMAGE_SHOULD_BE.getOrDefault(sender.getUUID(),10f);
                mob.getPersistentData().putBoolean("star_extra_hurt",true);
                StarDargonHit.setFreezeTick(view,20);
                var ammo=new StarDargonAmmo(sender,sender.getLevel(),mob.blockPosition(),damageShouldBe,Math.min(0.21f+(starDust / 100f * 0.01f),0.78f));
                sender.getLevel().playSound(null,sender, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.AMBIENT,1f,1f);
                sender.getLevel().addFreshEntity(ammo);
            }
        });
        return true;
    }
}
