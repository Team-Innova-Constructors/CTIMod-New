package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.method.GetModifierLevel;
import dev.xkmc.l2complements.events.MagicEventHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.modifiers.ModifierId;

@Mixin(MagicEventHandler.class)
public class CurseMixin {
    @Inject(method = "onHeal",at = @At("HEAD"), cancellable = true,remap = false)
    private static void chose(LivingHealEvent event, CallbackInfo ci){
        var entity=event.getEntity();
        var mari=new ModifierId("solidarytinker:heallight");
        if(entity instanceof Player player){
            boolean shouldCancel= GetModifierLevel.EquipHasModifierlevel(player,mari);
            if(shouldCancel){
                ci.cancel();
            }
        }
    }
}
