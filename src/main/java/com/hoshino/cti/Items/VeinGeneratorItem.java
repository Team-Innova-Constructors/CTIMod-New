package com.hoshino.cti.Items;

import blusunrize.immersiveengineering.api.excavator.ExcavatorHandler;
import blusunrize.immersiveengineering.api.excavator.MineralMix;
import blusunrize.immersiveengineering.api.excavator.MineralVein;
import blusunrize.immersiveengineering.common.IESaveData;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class VeinGeneratorItem extends Item {
    public VeinGeneratorItem(Properties properties, ResourceLocation mineralResourceLocation) {
        super(properties);
        this.resourceLocation=mineralResourceLocation;
    }
    private final ResourceLocation resourceLocation;

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
    @Override
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
    private MineralMix getMineralMix(Level level){
        MineralMix mineralMix=null;
        for(MineralMix mix:MineralMix.RECIPES.getRecipes(level)){
            if(mix.getId().equals(resourceLocation)){
                mineralMix=mix;
                break;
            }
        }
        return mineralMix;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if(!(living instanceof ServerPlayer serverPlayer))return stack;
        BlockHitResult rtr = getPlayerPOVHitResult(level, serverPlayer, ClipContext.Fluid.NONE);
        BlockPos pos = rtr.getBlockPos();
        ColumnPos columnPos=new ColumnPos(pos.getX(),pos.getZ());
        MineralVein vein=new MineralVein(columnPos,resourceLocation,15);
        ExcavatorHandler.addVein(serverPlayer.getLevel().dimension(), vein);
        IESaveData.markInstanceDirty();
        stack.shrink(1);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if(getMineralMix(level)!=null){
            MineralVein vein=new MineralVein(new ColumnPos(1,1),resourceLocation,15);
            var mineral=vein.getMineral(level);
            if(mineral!=null){
                var str=Language.getInstance().getOrDefault(mineral.getTranslationKey());
                list.add(Component.literal("长按右键在目标点生成"+str+ "矿脉").withStyle(style -> style.withColor(0xffaaff)));
            }
            else list.add(Component.literal("长按右键在目标点生成矿脉").withStyle(style -> style.withColor(0xffaaff)));
        }
    }
}
