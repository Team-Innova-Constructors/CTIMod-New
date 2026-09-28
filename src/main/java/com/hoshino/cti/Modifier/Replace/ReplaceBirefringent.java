package com.hoshino.cti.Modifier.Replace;

import com.hoshino.cti.api.interfaces.IModifierWithSpecialDesc;
import com.hoshino.cti.content.entityTicker.EntityTickerInstance;
import com.hoshino.cti.content.entityTicker.EntityTickerManager;
import com.hoshino.cti.library.modifier.CtiModifierHook;
import com.hoshino.cti.library.modifier.hooks.LeftClickModifierHook;
import com.hoshino.cti.register.CtiEntityTickers;
import com.hoshino.cti.util.CommonUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class ReplaceBirefringent extends NoLevelsModifier implements LeftClickModifierHook, ToolStatsModifierHook, IModifierWithSpecialDesc {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, CtiModifierHook.LEFT_CLICK, ModifierHooks.TOOL_STATS);
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.ATTACK_SPEED.multiply(builder,0.9f);
        ToolStats.ATTACK_DAMAGE.multiply(builder,0.9f);
    }

    @Override
    public void onLeftClickEntity(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, Entity target) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide&&RANDOM.nextFloat()<0.8f){
            var manager = EntityTickerManager.getInstance(player);
            if (!manager.hasTicker(CtiEntityTickers.BIREFRINGENT_TICKER.get())){
                manager.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.BIREFRINGENT_TICKER.get(),player.attackStrengthTicker, (int) (CommonUtil.getPlayerAttackDelay(player) *0.66f)));
            }
        }
    }

    @Override
    public void onLeftClickBlock(IToolStackView tool, ModifierEntry entry, Player player, Level level, EquipmentSlot equipmentSlot, BlockState state, BlockPos pos) {
        if (player.getAttackStrengthScale(0)>0.8&&!level.isClientSide&&RANDOM.nextFloat()<0.8f){
            var manager = EntityTickerManager.getInstance(player);
            if (!manager.hasTicker(CtiEntityTickers.BIREFRINGENT_TICKER.get())){
                manager.addTickerSimple(new EntityTickerInstance(CtiEntityTickers.BIREFRINGENT_TICKER.get(),player.attackStrengthTicker, (int) (CommonUtil.getPlayerAttackDelay(player) *0.66f)));
            }
        }
    }

    @Override
    public List<String> getDesc() {
        return List.of("info.cti.true_melee");
    }
}
