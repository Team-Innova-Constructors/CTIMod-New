package com.hoshino.cti.Modifier.Replace;

import com.c2h6s.etshtinker.Modifiers.modifiers.EtSTBaseModifier;
import com.hoshino.cti.Cti;
import com.hoshino.cti.register.CtiModifiers;
import com.xiaoyue.tinkers_ingenuity.content.library.helper.BreakLogicHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.context.ToolHarvestContext;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.definition.module.aoe.AreaOfEffectIterator;
import slimeknights.tconstruct.library.tools.helper.ToolHarvestLogic;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.BlockSideHitListener;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = Cti.MOD_ID)
public class FixVoidTouch extends EtSTBaseModifier {
    @SubscribeEvent
    public static void onLeftBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        BlockPos pos = event.getPos();
        Level world = player.getLevel();
        BlockState state = world.getBlockState(pos);
        if (!player.isCreative() && !player.hasEffect(MobEffects.DIG_SLOWDOWN) && state.getDestroySpeed(world, pos) < 0) {
            ToolStack tool = getHeldTool(player, InteractionHand.MAIN_HAND);
            if (tool == null || tool.isBroken() || tool.getModifierLevel(CtiModifiers.VOID_TOUCH.get()) < 1) {
                return;
            }

            state.getBlock().playerWillDestroy(world, pos, state, player);
            if (world instanceof ServerLevel serverLevel) {
                ItemStack toolStack = player.getMainHandItem();
                Direction sideHit = BlockSideHitListener.getSideHit(player);
                ToolHarvestContext context = new ToolHarvestContext(serverLevel, player, state, pos, sideHit, true, true);
                if (BreakLogicHelper.breakBlock(tool, toolStack, context)) {
                    Iterable<BlockPos> extraBlocks = tool.getHook(ToolHooks.AOE_ITERATOR).getBlocks(tool, toolStack, player, state, world, pos, BlockSideHitListener.getSideHit(player), AreaOfEffectIterator.AOEMatchType.BREAKING);
                    Iterator var12 = extraBlocks.iterator();

                    while (var12.hasNext()) {
                        BlockPos extraPos = (BlockPos) var12.next();
                        BlockState extraState = world.getBlockState(extraPos);
                        if (!extraState.isAir()) {
                            BreakLogicHelper.breakBlock(tool, toolStack, context.forPosition(extraPos.immutable(), extraState));
                        }
                    }

                    var12 = tool.getModifierList().iterator();

                    while (var12.hasNext()) {
                        ModifierEntry entry = (ModifierEntry) var12.next();
                        entry.getHook(ModifierHooks.BLOCK_HARVEST).finishHarvest(tool, entry, context, true);
                    }

                    BreakLogicHelper.dropItems(state, pos, serverLevel);
                }
            }
        }
    }

    @Override
    public void postMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage) {
        if (damage>0){
            DamageSource source = new EntityDamageSource(DamageSource.OUT_OF_WORLD.msgId, context.getAttacker());
            context.getTarget().hurt(source,damage*0.25f);
        }
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
}
