package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.misc.GloryEmblem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = GloryEmblem.class,remap = false)
public class GloryEmblemMixin {
    @Inject(method = "onAfterMeleeHit",cancellable = true,at = @At("HEAD"))
    public void cancelIfNotFullyCharged(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt, CallbackInfo ci){
        if (!context.isFullyCharged()) ci.cancel();
    }
}
