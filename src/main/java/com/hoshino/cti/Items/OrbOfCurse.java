package com.hoshino.cti.Items;

import com.hoshino.cti.register.CtiTab;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.logic.LevelEditor;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class OrbOfCurse extends Item {
    public OrbOfCurse() {
        super(new Properties().tab(CtiTab.MIXC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide){
            PlayerDifficulty cap = PlayerDifficulty.HOLDER.get(player);
            LevelEditor editor = cap.getLevelEditor();
            editor.addBase(10000);
            cap.sync();
            stack.shrink(1);
        }
        Random random = new Random();
        player.playSound(SoundEvents.ITEM_PICKUP,1, random.nextFloat()+0.5f);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_BOTTLE_CURSE.get(10000).withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("cti.tooltip.modifier.be_aware").withStyle(ChatFormatting.RED));
    }


}
