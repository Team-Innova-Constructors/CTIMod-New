package com.hoshino.cti.mixin;

import com.hoshino.cti.register.CtiModifiers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.mantle.client.SafeClientAccess;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin(LightTexture.class)
public class LightTextureMixin {
    @Inject(method = "getBrightness",cancellable = true,at = @At("HEAD"))
    private static void boostBrightness(DimensionType pDimensionType, int pLightLevel, CallbackInfoReturnable<Float> cir){
        var player = SafeClientAccess.getPlayer();
        if (player!=null){
            for (var stack:player.getArmorSlots())
                if (stack.getItem() instanceof IModifiable&& ToolStack.from(stack).getModifierLevel(CtiModifiers.BRIGHTNESS_BOOST.getId())>0){
                    cir.setReturnValue(1f);
                    break;
                }
        }
    }
}
