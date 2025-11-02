package com.hoshino.cti.Items;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import com.hoshino.cti.util.ExcavatorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;

public class VeinRemoveTool extends Item {
    public VeinRemoveTool(Properties properties) {
        super(properties);
    }
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();
        var pos = context.getClickedPos();
        var level = context.getLevel();
        if (player == null || level.getBlockState(pos).isAir() || stack.isEmpty()) {
            return InteractionResult.PASS;
        } else {
            player.startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 40;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if(living instanceof ServerPlayer player){
            BlockHitResult rtr = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockPos pos = rtr.getBlockPos();
            MineralVein vein = ExcavatorHandler.getRandomMineral(level, pos);
            if(vein!=null){
                var mineral=vein.getMineral(level);
                if(mineral!=null){
                    Vec2 vecToCenter = new Vec2((float) (vein.getPos().x() - pos.getX()), (float) (vein.getPos().z() - pos.getZ()));
                    if(vecToCenter.x == 0.0F && vecToCenter.y == 0.0F){
                        var str= Language.getInstance().getOrDefault(mineral.getTranslationKey());
                        var dimension=player.getLevel().dimension();
                        ExcavatorHelper.removeVein(dimension,vein);
                        player.displayClientMessage(Component.literal("已成功移除"+dimension+"维度"+"x:"+pos.getX()+",z:"+pos.getZ()+"的"+str).withStyle(style -> style.withColor(0x45f6ff)),true);
                    }
                    else player.displayClientMessage(Component.literal("请移动到矿脉中心点以移除此矿脉").withStyle(style -> style.withColor(0x9557ff)),true);
                }
            }
            else player.displayClientMessage(Component.literal("附近没有矿脉喵").withStyle(style -> style.withColor(0xd1a9ff)),true);
        }
        return stack;
    }
}
