package com.hoshino.cti.Items;

import com.google.common.collect.Lists;
import com.hoshino.cti.register.CtiTab;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Iterator;

public class RecipeTestItem extends Item {
    public RecipeTestItem() {
        super(new Item.Properties().tab(CtiTab.MIXC).stacksTo(1));
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity living) {
        if (!level.isClientSide) {
            living.sendSystemMessage(Component.literal("开始检查配方").withStyle(ChatFormatting.AQUA));
            int count1 = 0;
            for (RecipeType recipeType : ForgeRegistries.RECIPE_TYPES) {
                count1 += level.getRecipeManager().getAllRecipesFor(recipeType).size();
            }
            living.sendSystemMessage(Component.literal("重载前有 " + count1 + " 个配方").withStyle(ChatFormatting.AQUA));
            MinecraftServer server = level.getServer();
            if (server != null) {
                living.sendSystemMessage(Component.literal("开始重载").withStyle(ChatFormatting.AQUA));
                PackRepository repository = server.getPackRepository();
                WorldData data = server.getWorldData();
                Collection<String> collection1 = repository.getSelectedIds();
                Collection<String> collection2 = discoverNewPacks(repository, data, collection1);
                reloadPacks(collection2, server);
                int count2 = 0;
                for (RecipeType recipeType : ForgeRegistries.RECIPE_TYPES) {
                    count2 += level.getRecipeManager().getAllRecipesFor(recipeType).size();
                }
                if (count2 != count1) {
                    living.sendSystemMessage(Component.literal("重载前后配方数量不一致：" + count1 + " | " + count2 + " 掉配方了！").withStyle(ChatFormatting.RED));
                } else {
                    living.sendSystemMessage(Component.literal("重载前后配方数量一致：" + count1 + " | " + count2).withStyle(ChatFormatting.GREEN));
                }
            }
        }

        return stack;
    }

    public static void reloadPacks(Collection<String> p_138236_, MinecraftServer server) {
        server.reloadResources(p_138236_).exceptionally((p_138234_) -> {
            LOGGER.warn("Failed to execute reload", p_138234_);
            return null;
        });
    }

    public static Collection<String> discoverNewPacks(PackRepository repository, WorldData data, Collection<String> collection) {
        repository.reload();
        Collection<String> $$3 = Lists.newArrayList(collection);
        Collection<String> $$4 = data.getDataPackConfig().getDisabled();
        Iterator var5 = repository.getAvailableIds().iterator();

        while (var5.hasNext()) {
            String $$5 = (String) var5.next();
            if (!$$4.contains($$5) && !$$3.contains($$5)) {
                $$3.add($$5);
            }
        }

        return $$3;
    }

    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }

    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.BLOCK;
    }
}
