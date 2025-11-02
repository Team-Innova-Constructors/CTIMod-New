package com.hoshino.cti.mixin.Eni;

import com.aizistral.enigmaticlegacy.handlers.EnigmaticEventHandler;
import com.hoshino.cti.util.CurseUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EnigmaticEventHandler.class,remap = false)
public class EniEventMixin{
    @Redirect(method = "onPlayerTick(Lnet/minecraftforge/event/entity/living/LivingEvent$LivingTickEvent;)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;m_7311_(I)V"))
    private void setFire(Player instance, int i){
        if(instance instanceof ServerPlayer player){
            var time= CurseUtil.curseTime(player);
            if(time<192000){
                return;
            }
            instance.setRemainingFireTicks(i);
        }
    }
}
