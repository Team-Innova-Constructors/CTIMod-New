package com.hoshino.cti.mixin.MekMixin;

import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(remap = false, value = ItemUpgrade.class)
public class ItemUpgradeMixin {
    /**
     * @author EtSH_C2H6S
     * @reason 限制插件插入数量
     */
    @Overwrite
    public @NotNull InteractionResult m_6225_(UseOnContext context) {
        ItemUpgrade itemUpgrade = (ItemUpgrade) (Object) this;
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown()) {
            Level world = context.getLevel();
            BlockEntity tile = WorldUtils.getTileEntity(world, context.getClickedPos());
            if (tile instanceof IUpgradeTile upgradeTile) {
                if (upgradeTile.supportsUpgrades()) {
                    TileComponentUpgrade component = upgradeTile.getComponent();
                    ItemStack stack = context.getItemInHand();
                    Upgrade type = itemUpgrade.getUpgradeType(stack);
                    if (component.supports(type)) {
                        if (!world.isClientSide) {
                            int toAdd = Math.min(8 - component.getUpgrades(type), stack.getCount());
                            if (toAdd > 0 && toAdd <= 8) {
                                int added = component.addUpgrades(type, toAdd);
                                if (added > 0) {
                                    stack.shrink(added);
                                }
                            }
                        }

                        return InteractionResult.sidedSuccess(world.isClientSide);
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }
}
