package com.hoshino.cti.mixin.TIMixin;

import com.xiaoyue.tinkers_ingenuity.modifiers.tool.Rebound;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mixin(value = Rebound.class,remap = false)
public class ReboundMixin {
    @Inject(method = "onShieldBlocked",cancellable = true,at = @At("HEAD"))
    public void cancelIfThorn(IToolStackView tool, ShieldBlockEvent event, Player player, DamageSource source, int level, CallbackInfo ci){
        if (source instanceof EntityDamageSource eds&&eds.isThorns()) ci.cancel();
    }

    @Redirect(method = "onShieldBlocked",at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/library/tools/helper/ToolAttackUtil;extraEntityAttack(Lslimeknights/tconstruct/library/tools/nbt/IToolStackView;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean doThornDamage(IToolStackView tool, LivingEntity attackerLiving, InteractionHand hand, Entity targetEntity){
        return targetEntity.hurt(EntityDamageSource.thorns(attackerLiving), (float) (attackerLiving.getAttributeValue(Attributes.ATTACK_DAMAGE)*0.5f));
    }
}
