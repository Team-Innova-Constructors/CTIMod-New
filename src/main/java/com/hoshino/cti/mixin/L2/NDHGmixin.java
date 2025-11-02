package com.hoshino.cti.mixin.L2;

import com.hoshino.cti.util.CtiTagkey;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.events.MobEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(value = MobEvents.class,remap = false)
public class NDHGmixin {
    @Inject(method = "onMobDrop",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;m_32055_()Lnet/minecraft/world/item/ItemStack;"),locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void condition(LivingDropsEvent event, CallbackInfo ci, MobTraitCap cap, LivingEntity killer, double val, int count, Iterator<ItemStack> var6, ItemEntity stack){
        //后续如果有想禁用的加黑名单
        var item=stack.getItem().getItem();
        if(item instanceof BackpackItem||stack.getItem().is(CtiTagkey.NDHGBLACKLIST)){
            ci.cancel();
        }
    }
}
