package com.hoshino.cti.mixin.TwiMixin;

import com.aizistral.enigmaticlegacy.handlers.SuperpositionHandler;
import com.hoshino.cti.util.CurseUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.LampOfCindersItem;

@Mixin(LampOfCindersItem.class)
public abstract class LampMixin extends Item {
    public LampMixin(Properties p_41383_) {
        super(p_41383_);
    }
    @Inject(method = "releaseUsing",at = @At("HEAD"))
    private void onUse(ItemStack stack, Level level, LivingEntity living, int useRemaining, CallbackInfo ci){
        if(!(living instanceof Player player))return;
        if(!SuperpositionHandler.isTheCursedOne(player))return;
        if(CurseUtil.whetherAetherAllow(player)) return;
        CurseUtil.setAetherAllow(player,true);
    }
}
