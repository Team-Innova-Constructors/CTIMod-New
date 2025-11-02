package com.hoshino.cti.mixin.IEMixin;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import blusunrize.immersiveengineering.common.items.IEBaseItem;
import blusunrize.immersiveengineering.common.items.SurveyToolsItem;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = SurveyToolsItem.class,remap = false)
public abstract class SurveyToolsItemMixin extends IEBaseItem{
    /**
     * @author firefly
     * @reason 原本限制方块太难用了, 现在不再限制方块
     */
    @Overwrite
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

    /**
     * @author firefly
     * @reason 受够了原本的繁文缛节
     */
    @Overwrite
    public ItemStack m_5922_(ItemStack stack, Level world, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayer player) {
            BlockHitResult rtr = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
            BlockPos pos = rtr.getBlockPos();
            MineralVein vein = ExcavatorHandler.getRandomMineral(world, pos);
            if(vein!=null){
                var targetMineral=vein.getMineral(world);
                if (targetMineral != null) {
                    var str= Language.getInstance().getOrDefault(targetMineral.getTranslationKey());
                    Vec2 vecToCenter = new Vec2((float) (vein.getPos().x() - pos.getX()), (float) (vein.getPos().z() - pos.getZ()));
                    Component response;
                    var veinPos = vein.getPos();
                    if (vecToCenter.x == 0.0F && vecToCenter.y == 0.0F) {
                        response = Component.literal("你指向的地方就是"+str+"中心喵").withStyle(style -> style.withColor(0xffaa7f));
                    } else {
                        response = Component.literal("最近的"+str+"的中心点").append("在" +"x:"+ veinPos.x() + "," +"z:"+ veinPos.z()).withStyle(style -> style.withColor(0xc6fffe));
                    }
                    player.displayClientMessage(response, true);
                    world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.NEUTRAL, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
                    stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(user.getUsedItemHand()));
                }
                else player.displayClientMessage(Component.literal("呜喵~这个地方附近没有矿脉喵,换个地方试试吧").withStyle(style -> style.withColor(0xaaaaff)), true);
            }
            else player.displayClientMessage(Component.literal("呜喵~这个地方附近没有矿脉喵,换个地方试试吧").withStyle(style -> style.withColor(0xaaaaff)), true);
        }
        return stack;
    }
}
