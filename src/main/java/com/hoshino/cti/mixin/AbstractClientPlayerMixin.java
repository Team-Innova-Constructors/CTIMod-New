package com.hoshino.cti.mixin;

import com.hoshino.cti.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
    @Inject(at = @At(value = "RETURN"), method = "getFieldOfViewModifier", cancellable = true)
    public void resetModifier(CallbackInfoReturnable<Float> cir) {
        Player player = (AbstractClientPlayer) (Object) this;
        float f = 1f;
        if (player != null && EntityUtil.isAntiStun(player)) {
            ItemStack itemstack = player.getUseItem();
            if (player.isUsingItem()) {
                if (itemstack.is(Items.BOW)) {
                    int i = player.getTicksUsingItem();
                    float f1 = (float) i / 20.0F;
                    if (f1 > 1) {
                        f1 = 1;
                    } else {
                        f1 *= f1;
                    }

                    f = 1 - f1 * 0.15F;
                } else if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && player.isScoping()) {
                    cir.setReturnValue(0.1F);
                }
            }
            cir.setReturnValue(ForgeHooksClient.getFieldOfViewModifier(player, f));
        }
    }
}
