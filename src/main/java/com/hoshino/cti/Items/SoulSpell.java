package com.hoshino.cti.Items;

import com.hoshino.cti.util.CurseUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulSpell extends Item {
    public SoulSpell(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player=context.getPlayer();
        if(player==null)return InteractionResult.PASS;
        var fre= CurseUtil.getDeathFrequency(player);
        if(fre>0){
            player.startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if(living instanceof Player player){
            int fre=CurseUtil.getDeathFrequency(player);
            int time=CurseUtil.getPunishTime(player);
            if(fre>0||time>0){
                CurseUtil.setDeathFrequency(player,0);
                CurseUtil.setPunishTime(player,0);
                return new ItemStack(Items.GLASS_BOTTLE);
            }
        }
        return stack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public Component getName(ItemStack p_41458_) {
        return Component.literal("灵魂补剂").withStyle(style -> style.withColor(0xcb2706));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.literal("一瓶灵魂补剂,散发着灵魂的气息").withStyle(style -> style.withColor(0x904f00)));
        list.add(Component.literal("喝下它可以直接清除你的灵魂破碎状态,并且重置次数,并且消耗掉这瓶药水").withStyle(style -> style.withColor(0x64e794)));
        list.add(Component.literal("哼哼哼...那么代价是什么呢").withStyle(style -> style.withColor(0xe7113f)));
    }
}
