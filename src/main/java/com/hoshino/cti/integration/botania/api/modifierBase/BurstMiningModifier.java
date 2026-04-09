package com.hoshino.cti.integration.botania.api.modifierBase;

import com.hoshino.cti.integration.botania.api.CtiBotModifierHooks;
import com.hoshino.cti.integration.botania.api.hook.BurstHitModifierHook;
import com.hoshino.cti.integration.botania.api.hook.LensProviderModifierHook;
import com.hoshino.cti.integration.botania.api.interfaces.IManaBurstExtra;
import com.hoshino.cti.integration.botania.modifiers.FartherSights;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.helper.ToolHarvestLogic;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.common.item.BotaniaItems;

import java.util.List;
//用ModifierTraitModule附加这个
//让魔力脉冲能破坏方块
public class BurstMiningModifier extends Modifier implements LensProviderModifierHook, BurstHitModifierHook {
    @Override
    public boolean shouldDisplay(boolean advanced) {
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, CtiBotModifierHooks.LENS_PROVIDER,CtiBotModifierHooks.BURST_HIT);
    }

    @Override
    public void burstHitBlock(@Nullable IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, @Nullable Entity owner, Level level, BlockPos blockPos, Direction direction, boolean isManaBlock, boolean shouldKill, ManaBurst burst, IManaBurstExtra burstExtra) {
        if (burst.entity().getTags().contains(FartherSights.KEY_TRIGGER_TOOL)&&tool!=null&&level instanceof ServerLevel&&owner instanceof Player player){
            ItemStack stack = ((ToolStack)tool).createStack();
            BlockState state = level.getBlockState(blockPos);
            if (state.canHarvestBlock(level, blockPos, player)){
                if (ToolHarvestLogic.handleBlockBreak(stack,blockPos,player)){
                    ToolHarvestContext context = new ToolHarvestContext((ServerLevel) level, player, state, blockPos, direction, true, true);
                    for (ModifierEntry entry : tool.getModifierList()) {
                        entry.getHook(ModifierHooks.BLOCK_BREAK).afterBlockBreak(tool, entry, context);
                    }
                }
            }
        }
    }

    @Override
    public List<ItemStack> getLens(IToolStackView tool, ModifierEntry modifier, List<ModifierEntry> modifierList, ManaBurst burst, IManaBurstExtra burstExtra) {
        return burst.entity().getTags().contains(FartherSights.KEY_TRIGGER_TOOL)?List.of():List.of(new ItemStack(BotaniaItems.lensMine));
    }
}
