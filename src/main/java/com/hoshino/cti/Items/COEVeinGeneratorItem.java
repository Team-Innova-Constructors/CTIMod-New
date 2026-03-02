package com.hoshino.cti.Items;

import com.tom.createores.OreDataCapability;
import com.tom.createores.recipe.ExcavatingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class COEVeinGeneratorItem extends Item {
    public COEVeinGeneratorItem() {
        super(new Item.Properties().stacksTo(1));
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
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (!level.isClientSide) {
            CompoundTag nbt = stack.getTag();
            if(living instanceof ServerPlayer serverPlayer){
                if (nbt != null && nbt.contains("MineralType")) {
                    LevelChunk chunk = level.getChunkAt(serverPlayer.getOnPos());
                    OreDataCapability.OreData data = OreDataCapability.getData(chunk);
                    data.setRecipe(new ResourceLocation(getTypeString(nbt)));
                    data.setExtractedAmount(getExtractedAmount(nbt));
                    data.setRandomMul(getMul(nbt));
                    data.setLoaded(true);
                    chunk.setUnsaved(true);
                    stack.shrink(1);
                    serverPlayer.displayClientMessage(Component.literal("成功将矿脉生成！").withStyle(style -> style.withColor(0x8eff74)), true);
                    return stack;
                }
            }
        }
        return stack;
    }
    private String getTypeString(CompoundTag nbt){
        return nbt.getString("MineralType");
    }
    private long getExtractedAmount(CompoundTag nbt){
        return nbt.getLong("ExtAmount");
    }
    private float getMul(CompoundTag nbt){
        return nbt.getFloat("Mul");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        var nbt=stack.getTag();
        if(level==null)return;
        if (nbt != null && nbt.contains("MineralType")){
            String localizedName;
            ResourceLocation mineralId = new ResourceLocation(getTypeString(nbt));
            RecipeManager rm = level.getRecipeManager();
            Optional<? extends Recipe<?>> recipe = rm.byKey(mineralId);
            if (recipe.isPresent() && recipe.get() instanceof ExcavatingRecipe er) {
                 localizedName = er.getId().toString();
            } else {
                localizedName=mineralId.toLanguageKey();
            }
            components.add(Component.literal("矿脉种类:"+localizedName));
            components.add(Component.literal("矿脉消耗量:"+getExtractedAmount(nbt)));
        }
    }
}
