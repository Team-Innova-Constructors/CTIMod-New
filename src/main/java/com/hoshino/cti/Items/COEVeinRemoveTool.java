package com.hoshino.cti.Items;

import com.hoshino.cti.mixin.COEMixin.OreDataAccessor;
import com.hoshino.cti.register.CtiItem;
import com.hoshino.cti.register.CtiTab;
import com.tom.createores.OreDataCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class COEVeinRemoveTool extends Item{
    public COEVeinRemoveTool() {
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
        if (!level.isClientSide && living instanceof ServerPlayer player) {
            BlockPos pos = player.blockPosition();
            LevelChunk chunk = level.getChunkAt(pos);
            OreDataCapability.OreData data = OreDataCapability.getData(chunk);
            if (data != null && data.getRecipeId() != null) {
                ItemStack seedStack = findPumpkinSeed(player);

                if (!seedStack.isEmpty()) {
                    ItemStack generator = new ItemStack(CtiItem.COE_VEIN_SEED.get());
                    CompoundTag nbt = new CompoundTag();
                    var dataAccessor=(OreDataAccessor)data;
                    nbt.putString("MineralType", data.getRecipeId().toString());
                    nbt.putLong("ExtAmount", dataAccessor.getExtractedAmount());
                    nbt.putFloat("Mul", dataAccessor.getRandomMul());
                    generator.setTag(nbt);
                    data.setRecipe(null);
                    data.setExtractedAmount(0L);
                    data.setLoaded(true);
                    chunk.setUnsaved(true);
                    seedStack.shrink(1);
                    if (!player.getInventory().add(generator)) {
                        player.drop(generator, false);
                    }
                    player.displayClientMessage(Component.literal("矿脉已提取"), true);
                } else {
                    player.displayClientMessage(Component.literal("缺少南瓜种子"), true);
                }
                return stack;
            }
            player.displayClientMessage(Component.literal("玩家所在区块中没有矿脉"), true);
        }
        return stack;
    }
    private ItemStack findPumpkinSeed(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(Items.PUMPKIN_SEEDS)) return s;
        }
        return ItemStack.EMPTY;
    }
}
