package com.hoshino.cti.mixin;

import com.hoshino.cti.util.SkullHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(PlayerHeadItem.class)
public class PlayerSkullMixin extends StandingAndWallBlockItem {

    public PlayerSkullMixin(Block p_43248_, Block p_43249_, Properties p_43250_) {
        super(p_43248_, p_43249_, p_43250_);
    }
    @Unique
    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 200;
    }
    @Unique
    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.EAT;
    }
    @Unique
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var player = context.getPlayer();
        var pos = context.getClickedPos();
        var level = context.getLevel();
        //恶徒水壶的UUID
        if (player == null || !SkullHelper.getPlayerUUIDFromHead(stack).equals(UUID.fromString("f0692b01-fa6d-44c5-9aa9-93dd2d233c96")) || level.getBlockState(pos).isAir()) {
            return InteractionResult.PASS;
        } else {
            player.startUsingItem(context.getHand());
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        living.addEffect(new MobEffectInstance(MobEffects.LEVITATION,1000,0));
        stack.shrink(1);
        return stack;
    }
}
