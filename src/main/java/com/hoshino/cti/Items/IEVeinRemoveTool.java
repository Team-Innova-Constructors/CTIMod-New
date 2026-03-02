package com.hoshino.cti.Items;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import blusunrize.immersiveengineering.common.IESaveData;
import com.hoshino.cti.mixin.IEMixin.VeinAccessor;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.register.CtiTab;
import com.hoshino.cti.util.ExcavatorHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;

public class IEVeinRemoveTool extends Item {
    public IEVeinRemoveTool() {
        super(new Item.Properties().stacksTo(1).tab(CtiTab.MIXC));
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
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (living instanceof ServerPlayer player) {
            BlockHitResult rtr = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockPos pos = rtr.getBlockPos();
            MineralVein vein = ExcavatorHandler.getRandomMineral(level, pos);
            if (vein != null) {
                var mineral = vein.getMineral(level);
                int depletion = vein.getDepletion();
                if (mineral != null) {
                    Vec2 vecToCenter = new Vec2((float) (vein.getPos().x() - pos.getX()), (float) (vein.getPos().z() - pos.getZ()));
                    if (vecToCenter.x == 0.0F && vecToCenter.y == 0.0F) {
                        for (ItemStack itemStack : player.getInventory().items) {
                            if (itemStack.getItem() == Items.MELON_SEEDS) {
                                var text=Component.translatable(mineral.getTranslationKey());
                                var dimension = player.getLevel().dimension();
                                ItemStack generatorStack = new ItemStack(CtiItem.IE_VEIN_SEED.get());
                                CompoundTag tag = new CompoundTag();
                                var veinAccessor = (VeinAccessor) vein;
                                tag.putString("MineralType", veinAccessor.getMineralName().toString());
                                tag.putInt("Depletion", vein.getDepletion());
                                generatorStack.setTag(tag);
                                ExcavatorHelper.removeVein(dimension, vein);
                                itemStack.shrink(1);
                                if (!player.getInventory().add(generatorStack)) {
                                    player.drop(generatorStack, false);
                                }
                                IESaveData.markInstanceDirty();
                                player.displayClientMessage(Component.literal("已提取").append(text).append("，保留进度: " + depletion).withStyle(style -> style.withColor(0x45f6ff)), true);
                                return stack;
                            }
                        }
                        player.displayClientMessage(Component.literal("背包没有西瓜种子,没法提取喵").withStyle(style -> style.withColor(0x9557ff)), true);
                    } else
                        player.displayClientMessage(Component.literal("请移动到矿脉中心点以转移此矿脉").withStyle(style -> style.withColor(0x9557ff)), true);
                }
            } else
                player.displayClientMessage(Component.literal("这个中心没有矿脉喵").withStyle(style -> style.withColor(0xd1a9ff)), true);
        }
        return stack;
    }
}
