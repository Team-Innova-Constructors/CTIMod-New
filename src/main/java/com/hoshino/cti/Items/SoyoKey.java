package com.hoshino.cti.Items;

import com.hoshino.cti.client.CtiClientHooks;
import com.hoshino.cti.client.Screen.MorseCodeScreen;
import com.hoshino.cti.register.CtiBlock;
import com.hoshino.cti.register.CtiTab;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoyoKey extends Item {
    @Getter
    private final SoyoKeyCategory soyoKeyCategory;
    @Getter
    private final List<Component> components;

    public SoyoKey(SoyoKeyCategory soyoKeyCategory, List<Component> components) {
        super(new Item.Properties().tab(CtiTab.MIXC));
        this.soyoKeyCategory = soyoKeyCategory;
        this.components = components;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.PASS;

        if (state.is(CtiBlock.qi_yao_matrix.get())) {
            if (level.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    int index = this.soyoKeyCategory.ordinal();
                    CtiClientHooks.openMorseCodeScreen(pos, index);
                });
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.literal("对着七曜矩阵右键使用,会消耗掉该钥匙"));
        p_41423_.addAll(this.components);
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
