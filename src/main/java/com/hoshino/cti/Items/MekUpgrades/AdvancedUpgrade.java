package com.hoshino.cti.Items.MekUpgrades;

import com.hoshino.cti.Items.TooltipedItem;
import com.hoshino.cti.register.CtiTab;
import mekanism.api.Upgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdvancedUpgrade extends TooltipedItem {
    public final List<Upgrade> typeList;
    public final int upTo;
    public final boolean consume;

    public AdvancedUpgrade(int upTo, boolean consume, @NotNull List<Upgrade> typeList) {
        super(new Item.Properties().stacksTo(consume ? 64 : 1).tab(CtiTab.MIXC), List.of(Component.literal("Shift-右键机器时将对应升级的等级提升至 ").append(String.valueOf(upTo) + " 级").withStyle(ChatFormatting.AQUA), Component.literal(consume ? "消耗品" : "不消耗").withStyle(consume ? ChatFormatting.YELLOW : ChatFormatting.GREEN)));
        this.typeList = typeList;
        this.upTo = upTo;
        this.consume = consume;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Level world = context.getLevel();
            BlockEntity tile = WorldUtils.getTileEntity(world, context.getClickedPos());
            if (tile instanceof IUpgradeTile upgradeTile) {
                if (upgradeTile.supportsUpgrades()) {
                    TileComponentUpgrade component = upgradeTile.getComponent();
                    ItemStack stack = context.getItemInHand();
                    if (stack.getItem() instanceof AdvancedUpgrade upgrade && !upgrade.typeList.isEmpty()) {
                        boolean successful = false;
                        for (Upgrade type : upgrade.typeList) {
                            if (component.supports(type)) {
                                if (!world.isClientSide) {
                                    if (component.getUpgrades(type) > 0) {
                                        int toAdd = upgrade.upTo - component.getUpgrades(type);
                                        if (toAdd > 0) {
                                            component.addUpgrades(type, toAdd);
                                            successful = true;
                                            if (upgrade.consume) {
                                                stack.shrink(1);
                                            }
                                        }
                                    } else {
                                        component.addUpgrades(type, upgrade.upTo);
                                        successful = true;
                                        if (upgrade.consume) {
                                            stack.shrink(1);
                                        }
                                    }
                                }
                            }
                        }
                        if (successful) {
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
