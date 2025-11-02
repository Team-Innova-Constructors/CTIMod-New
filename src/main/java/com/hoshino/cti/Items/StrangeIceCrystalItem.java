package com.hoshino.cti.Items;

import com.aetherteam.aether.entity.projectile.crystal.IceCrystal;
import com.hoshino.cti.register.CtiTab;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StrangeIceCrystalItem extends Item {
    public StrangeIceCrystalItem() {
        super(new Properties().fireResistant().stacksTo(1).tab(CtiTab.MIXC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 1;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide){
            IceCrystal crystal = new IceCrystal(pLevel,pLivingEntity);
            crystal.setPos(pLivingEntity.getEyePosition());
            crystal.setDeltaMovement(pLivingEntity.getLookAngle().scale(2));
            pLivingEntity.level.addFreshEntity(crystal);
            if (pLivingEntity instanceof Player player) player.getCooldowns().addCooldown(pStack.getItem(),600);
        }
        return pStack;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("info.cti.strange_ice_crystal"));
    }
}
